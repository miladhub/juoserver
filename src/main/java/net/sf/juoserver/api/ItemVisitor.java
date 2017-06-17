package net.sf.juoserver.api;

public interface ItemVisitor {
	void visit(Item item);
	void visit(Container item);
}
