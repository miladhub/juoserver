package net.sf.juoserver.model;

import java.net.InetAddress;

public class ServerInfo {
	private String name;
	private InetAddress address;
	public ServerInfo(String name, InetAddress address) {
		super();
		this.name = name;
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public InetAddress getAddress() {
		return address;
	}
	@Override
	public String toString() {
		return "ServerInfo [address=" + address + ", name=" + name + "]";
	}
}
