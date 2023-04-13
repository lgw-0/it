import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import static java.lang.Math.*;

public class GenerateGraph{
	private int x_axis=50;
	private int y_axis=50;
	double div;
	private int scale=10;
	public BufferedImage image;
	
	public GenerateGraph(){}
	
	public void drawGraph(int width, int height){
		//创建图像栅格
		this.image=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster=image.getRaster();
		ColorModel model=image.getColorModel();
		
		Color curvecolor=Color.red;
		int argb=curvecolor.getRGB();
		//依据颜色模型获取颜色数据
		Object colordata=model.getDataElements(argb, null);
		
		int backgroundcolor[]={255, 255, 255, 255};//背景颜色
		int axiscolor[]={0, 0, 0, 255};//坐标轴颜色
		
		for (int i=0;i<width;i++){
			raster.setPixel(i, height-x_axis, axiscolor);
			if (i%scale==0){
				raster.setPixel(i, height-x_axis-1, axiscolor);
				raster.setPixel(i, height-x_axis-2, axiscolor);
				raster.setPixel(i, height-x_axis-3, axiscolor);
			}//may occur out of bound exception!
		}//绘制x轴
		
		for (int j=0;j<height;j++){
			raster.setPixel(y_axis, j, axiscolor);
			if (j%scale==0){
				raster.setPixel(y_axis+1, j, axiscolor);
				raster.setPixel(y_axis+2, j, axiscolor);
				raster.setPixel(y_axis+3, j, axiscolor);//
			}//may occur out of bound exception!
		}//绘制y轴
		
		for (int i=0-y_axis;i<width;i++){
			int j=height-x_axis-(int)f(i);//Round() is better?
			
			if (j<height & j>=0 & (i+y_axis<width)){
				//离散的像素点放入图像栅格
				raster.setDataElements(i+y_axis, j, colordata);
				//将离散的像素点描成线
				int param=height-x_axis-(int)f(i+1);
				if(j<param&param<height&param>0){
					for (int y=j;y<param;y++){
						raster.setDataElements(i+y_axis, y, colordata);
					}//递增
				}
				else if(j>param&param<height&param>0){
					for (int y=param;y<j;y++){
						raster.setDataElements(i+y_axis, y, colordata);
					}//递减
				}
			}
			else if (j>=height & (i+y_axis<width)){
				j=height;
				raster.setDataElements(i+y_axis, j, colordata);
			}
			else if (j<0 & (i+y_axis<width)){
				j=0;
				raster.setDataElements(i+y_axis, j, colordata);
			}
		}//绘制函数曲线
	}//绘制函数在直角坐标下的图像，由于图像栅格的坐标原点在左上方，所以实际坐标值要进行变换
	
	public void setX_axis(int x_axis){
		this.x_axis=x_axis;
	}//设置x轴位置
	
	public void setY_axis(int y_axis){
		this.y_axis=y_axis;
	}//设置y轴位置
	
	public void setUnit(double unitdiv){
		this.div=unitdiv*0.1;
	}//设置最小刻度值

	public double f(double X){
		double x=X*div;
		double y;
		double sigma=0.25;
		double mu=0.3;
		y=(1/(sigma*pow(2*3.14159, 0.5)))*pow(2.71828, -(x-mu)*(x-mu)/(2*sigma*sigma));//待绘制的函数
		return y/div;
	}
}
