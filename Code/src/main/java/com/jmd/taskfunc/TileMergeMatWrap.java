package com.jmd.taskfunc;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import com.jmd.common.StaticVar;

import lombok.Getter;

public class TileMergeMatWrap {

	private int tileWidth = StaticVar.TILE_WIDTH;
	private int tileHeight = StaticVar.TILE_HEIGHT;
	private Mat des = null;

	@Getter
	private long allPixel = 0L;
	@Getter
	private long runPixel = 0L;

	public void init(int width, int height) {
		/**
		 * CV_8uc1 单颜色通道 8位</br>
		 * CV_8uc2 2颜色通道 16位</br>
		 * CV_8uc3 3颜色通道 24位</br>
		 * CV_8uc4 4颜色通道 32位</br>
		 */
		// CV_8UC4为支持透明PNG的RGBA格式
		this.des = Mat.zeros(height, width, CvType.CV_8UC4);
		// 计算总像素数量
		this.allPixel = (long) width * (long) height;
	}

	public void mergeToMat(String file, int x, int y, boolean flag) {
		// 是否读取图像进行合并
		if (flag) {
			// 读取图片
			Mat tileMat = Imgcodecs.imread(file, Imgcodecs.IMREAD_UNCHANGED);
			// 转换图片至RGBA格式
			Imgproc.cvtColor(tileMat, tileMat, Imgproc.COLOR_BGR2BGRA);
			// 确定坐标位置
			Mat rectForDes = this.des.colRange(new Range(x, x + this.tileWidth))
					.rowRange(new Range(y, y + this.tileHeight));
			// 填充至合并大图
			tileMat.copyTo(rectForDes);
		}
		// 完成后计算已合并的像素数量
		this.runPixel += this.tileWidth * this.tileHeight;
	}

	public void output(String outputFile) {
		Imgcodecs.imwrite(outputFile, des);
	}

	public void destroy() {
		this.des.release();
		this.des = null;
	}

}
