package com.xsx.ncd.define;

import javafx.scene.image.Image;

public class DeviceIcoInfo {
	private String name;
	
	private Image image;

	public DeviceIcoInfo(String name, Image image) {
		super();
		this.name = name;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
