package com.jmd.entity.geo;

import lombok.Data;

@Data
public class Bound {

	private MercatorPoint topLeft;
	private MercatorPoint topRight;
	private MercatorPoint bottomLeft;
	private MercatorPoint bottomRight;

}
