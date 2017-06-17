package net.sf.juoserver.protocol;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.MessageDecoder;
import net.sf.juoserver.api.MessageDecoderProvider;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

/**
 * {@link MessageDecoderProvider} implementation that scans the classpath
 * for {@link Decodable} {@link Message}s upon instantiation.
 * <p/>
 * For performance reasons, the scanning is done only once.
 */
public class ClasspathMessageDecoderProvider implements MessageDecoderProvider {
	private static Map<Byte, MessageDecoder> decoders = null;
	
	public ClasspathMessageDecoderProvider() {
		super();
		init();
	}

	protected void init() {
		if (decoders == null) {
			configureFrom(ClasspathMessageDecoderProvider.class);
		}
	}

	protected void configureFrom(Class<?> baseClass) {
		decoders = new HashMap<Byte, MessageDecoder>();
		AnnotationDB adb = new AnnotationDB();
		adb.setScanFieldAnnotations(false);
		adb.setScanMethodAnnotations(false);
		adb.setScanParameterAnnotations(false);
		try {
			adb.scanArchives(ClasspathUrlFinder.findClassBase(baseClass));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		for (String decodableClass : adb.getAnnotationIndex().get(Decodable.class.getName())) {
			Class<?> clazz = null;
			try {
				clazz = baseClass.getClassLoader().loadClass(decodableClass);
				decoders.put((byte) (clazz.getAnnotation(Decodable.class).code() & 0xFF),
						getMessageDecoder( clazz ));
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			} catch (IllegalStateException e) {
				if (e.getCause() != null && e.getCause() instanceof NoSuchMethodException) {
					throw new BadDecodableException("Missing public(byte[]) constructor on message class: "
							+ clazz.getName(), e);
				} else {
					throw e;
				}
			}
		}
	}
	
	@Override
	public MessageDecoder getDecoder(byte firstByte) {
		return decoders.get(firstByte);
	}

	private MessageDecoder getMessageDecoder(Class<?> clazz) {
		final Constructor<?> c;
		try {
			c = clazz.getConstructor(byte[].class);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		}
		return new MessageDecoder() {
			@Override
			public Message decode(byte[] contents) {
				try {
					return (Message) c.newInstance(contents);
				} catch (InstantiationException e) {
					throw new MessageReaderException( getMessageDetails(contents), e );
				} catch (IllegalAccessException e) {
					throw new MessageReaderException( getMessageDetails(contents), e );
				} catch (InvocationTargetException e) {
					throw new MessageReaderException( getMessageDetails(contents), e );
				}
			}
		};
	}
	
	protected Map<Byte, MessageDecoder> getDecoders() { return decoders; }
	
	/**
	 * Returns a textual representation of the given message's
	 * details.
	 * 
	 * @param contents the message contents
	 * @return a textual representation of the given message's
	 * details
	 */
	private static String getMessageDetails(byte[] contents) {
		return "Code: " + MessagesUtils.getCodeHexString(contents) + ", contents: ["
		+ MessagesUtils.getHexString(contents) + "]";
	}

}
