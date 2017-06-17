package net.sf.juoserver.protocol;

import java.util.ArrayList;
import java.util.List;

import net.sf.juoserver.api.ItemVisitor;
import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;

public class ItemManager implements ItemVisitor {
	private List<Message> messages = new ArrayList<Message>();

	public List<Message> use(Item item) {
		item.accept(this);
		return messages;
	}

	@Override
	public void visit(Item item) {
	}
	
	@Override
	public void visit(Container item) {
		messages.add(new DrawContainer(item));
		messages.add(new ContainerItems(item));
	}
}
