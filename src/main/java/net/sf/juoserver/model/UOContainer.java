package net.sf.juoserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.juoserver.api.ItemVisitor;
import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Point2D;

public class UOContainer extends UOItem implements Container {
	private List<Item> items;
	private int gumpId;
	private Map<Item, Point2D> positions;
	
	public UOContainer(int serialId, int modelId, int hue, int gumpId, List<? extends Item> items,
			Map<Item, Point2D> positions) {
		super(serialId, modelId, hue);
		this.gumpId = gumpId;
		this.items = new ArrayList<Item>(items);
		this.positions = positions;
	}

	@Override
	public List<? extends Item> getItems() {
		return items;
	}

	@Override
	public int getGumpId() {
		return gumpId;
	}

	@Override
	public void accept(ItemVisitor itemManager) {
		itemManager.visit(this);
	}

	@Override
	public Point2D getPositionWithinContainer(Item item) {
		return positions.get(item);
	}
	
	@Override
	public Map<Item, Point2D> getItemsPositions() {
		return positions;
	}

	@Override
	public void removeItem(Item item) {
		items.remove(item);
	}

	@Override
	public void addItem(Item item, Point2D where) {
		items.add(item);
		positions.put(item, where);
	}

	@Override
	public String toString() {
		return "UOContainer [serialId=" + getSerialId() + ", items=" + items + "]";
	}
}
