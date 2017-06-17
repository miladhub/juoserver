package net.sf.juoserver.protocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.ProtocolController;

/**
 * Base controller class.
 * <p/>
 * This class adopts a modified version of the visitor pattern in order to
 * handle the {@link Message} request instances: instead of placing an
 * <tt>accept()</tt> method on every {@link Message} class and then having them
 * call something like <tt>visitor.handle( this )</tt>, the
 * {@link #getReply(Message)} method detects the specific subclass of the
 * {@link Message} parameter and invokes the right handler method. The same
 * pattern is adopted in order to implement the {@link #postProcess(Message)}
 * method.
 * <p/>
 * The detected methods are lazily saved into the {@link #handlers} and
 * {@link #postProcessors} maps, which are static according to the fly-weight
 * pattern, because they do not depend on the specific client whose conversation
 * is being controlled.
 * 
 * @see <a href="http://surguy.net/articles/visitor-with-reflection.xml">Visitor with Reflection</a>
 * @see <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip98.html">Java Tip</a>
 */
public abstract class AbstractProtocolController implements ProtocolController {
	private static final Logger logger = Logger.getLogger(AbstractProtocolController.class);
	
	private static final String HANDLE_METHOD_NAME = "handle";
	private static final String POST_PROCESSOR_METHOD_NAME = "postProcess";
	
	/**
	 * Message handlers.
	 */
	private static final Map<Class<? extends Message>, Method> handlers = 
			new ConcurrentHashMap<Class<? extends Message>, Method>();
	
	/**
	 * Message post-processors.
	 */
	private static final Map<Class<? extends Message>, Method> postProcessors = 
			new ConcurrentHashMap<Class<? extends Message>, Method>();
	
	@SuppressWarnings("unchecked")
	@Override
	public final List<Message> getReply(Message request) {
		try {
			if (!handlers.containsKey( request.getClass() )) {
				handlers.put(request.getClass(),
						getClass().getMethod(HANDLE_METHOD_NAME, request.getClass()));
			}
		} catch (NoSuchMethodException e) {
			logger.warn("Unprocessable client message: " + request);
			return null;
		}
		Method handler = handlers.get( request.getClass() );
		try {
			Object messages = handler.invoke( this, request );
			if (messages == null) {
				return null;
			}
			if (messages instanceof List) {
				return (List<Message>) messages;
			} else {
				return asList((Message) messages);
			}
		} catch (IllegalAccessException e) {
			throw new ProtocolException(e);
		} catch (InvocationTargetException e) {
			throw new ProtocolException(e);
		}
	}
	
	@Override
	public final void postProcess(Message request) {
		if (!postProcessors.containsKey( request.getClass() )) {
			try {
				postProcessors.put(request.getClass(),
						getClass().getMethod(POST_PROCESSOR_METHOD_NAME,
								request.getClass()));
			} catch (NoSuchMethodException e) {
			}
		}
		Method postProcessor = postProcessors.get( request.getClass() );
		if (postProcessor == null) {
			return;
		}
		try {
			postProcessor.invoke( this, request );
		} catch (IllegalAccessException e) {
			throw new ProtocolException(e);
		} catch (InvocationTargetException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected final List<Message> asList(Message... serverList) {
		return Arrays.asList(serverList);
	}

	@Override
	public boolean isInterestedIn(Message message) {
		try {
			getClass().getMethod(HANDLE_METHOD_NAME, message.getClass());
			return true;
		} catch (SecurityException e) {
			throw new ProtocolException(e);
		} catch (NoSuchMethodException e) {
			return false;
		}
	}
}
