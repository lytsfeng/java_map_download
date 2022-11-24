package com.jmd.entity.geo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tile implements Serializable {

	private static final long serialVersionUID = -3598715656639420131L;

	private Integer z;
	private Integer x;
	private Integer y;

	private MercatorPoint topLeft;
	private MercatorPoint topRight;
	private MercatorPoint bottomLeft;
	private MercatorPoint bottomRight;

	public Tile() {

	}

	public Tile(int z, int x, int y) {
		this.z = z;
		this.x = x;
		this.y = y;
	}

}
