package net.sf.juoserver.api;

public interface Item extends JUoEntity {
	void accept(ItemVisitor itemManager);
}
