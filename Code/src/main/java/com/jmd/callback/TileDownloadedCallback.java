package com.jmd.callback;

public interface TileDownloadedCallback {

	void execute(int z, String name, int count, int xRun, int yRun, boolean success);

}
