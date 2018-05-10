package com.imago.graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
public class VideoThumbnails {
	public static final double SECONDS_BETWEEN_FRAMES = 10;
	// The video stream index, used to ensure we display frames from one and

	// only one video stream from the media container.

	private static int mVideoStreamIndex = -1;

	// Time of last frame write
	private static long mLastPtsWrite = Global.NO_PTS;

	public static final long MICRO_SECONDS_BETWEEN_FRAMES =

			(long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

	public static ArrayList<Image> extractFrames(String inputFile,String dumpDirectory) {
//		File[] files = new File("/home/maggie/workspace/imago/image_dump/").listFiles();
//		for (File f: files) f.delete();
		

		IMediaReader mediaReader = ToolFactory.makeReader(inputFile);
		// stipulate that we want BufferedImages created in BGR bit color space

		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		mediaReader.addListener(new ImageSnapListener(dumpDirectory));
		// read out the contents of the media file and
		// dispatch events to the attached listener
		while (mediaReader.readPacket() == null) ;

		File[]	files = new File(dumpDirectory).listFiles();
	
		ArrayList<Image> thumbnails=readImages(files,dumpDirectory);   
		
		return thumbnails;
	}
	public static ArrayList<Image> readImages(File[] files,String dumpDirectory) {
		ArrayList<Image> thumbnails=new ArrayList<>();
		for (File file : files) {
			if (file.isDirectory()) {
			
				readImages(file.listFiles(),dumpDirectory); // Calls same method again.
			} else {

				Image current=Utilities.readImage(dumpDirectory+file.getName());
				thumbnails.add(current);
			}
		}
		return thumbnails;
	}
	 private static class ImageSnapListener extends MediaListenerAdapter {
		 String dumpDirectory="";	
		 public ImageSnapListener(String dumpDirectory){
		 		this.dumpDirectory=dumpDirectory;
		 	}
		 
	        public void onVideoPicture(IVideoPictureEvent event) {
	 
	 
	 
	            if (event.getStreamIndex() != mVideoStreamIndex) {
	 
	                // if the selected video stream id is not yet set, go ahead an
	 
	                // select this lucky video stream
	 
	                if (mVideoStreamIndex == -1) {
	                    mVideoStreamIndex = event.getStreamIndex();
	                } // no need to show frames from this video stream
	                else {
	                    return;
	                }
	 
	            }
	 
	 
	 
	            // if uninitialized, back date mLastPtsWrite to get the very first frame
	 
	 
	 
	            if (mLastPtsWrite == Global.NO_PTS) {
	                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
	            }
	 
	 
	 
	            // if it's time to write the next frame
	 
	            if (event.getTimeStamp() - mLastPtsWrite
	                    >= MICRO_SECONDS_BETWEEN_FRAMES) {
	 
	 
	 
	                String outputFilename = dumpImageToFile(event.getImage());
	 
	                // indicate file written
	 
	                double seconds = ((double) event.getTimeStamp())
	                        / Global.DEFAULT_PTS_PER_SECOND;
	 
	 
	                // update last write time
	 
	                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
	 
	            }
	 
	 
	 
	        }
	 
	        private String dumpImageToFile(BufferedImage image) {
	 
	            try {
	 
	                String outputFilename = dumpDirectory+"/"
	                        + System.currentTimeMillis() + ".jpg";
	 
	                ImageIO.write(image, "jpg", new File(outputFilename));
	 
	                return outputFilename;
	 
	            } catch (IOException e) {
	 
	                e.printStackTrace();
	 
	                return null;
	 
	            }
	 
	        }
	    }
}

