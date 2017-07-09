package com.pmqin.hadoop;
import java.io.IOException;

import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InRcFileMapper extends Mapper<LongWritable, BytesRefArrayWritable, Text, NullWritable> {

	@Override
	protected void map(LongWritable key, BytesRefArrayWritable value,
			Mapper<LongWritable, BytesRefArrayWritable, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		  Text txt = new Text();    
	        //因为RcFile行存储和列存储，所以每次进来的一行数据，Value是个列簇，遍历，输出。   
	            StringBuffer sb = new StringBuffer();   
	            for (int i = 0; i < value.size(); i++) {   
	                BytesRefWritable v = value.get(i);   
	                txt.set(v.getData(), v.getStart(), v.getLength());   
	                if(i==value.size()-1){   
	                    sb.append(txt.toString());   
	                }else{   
	                    sb.append(txt.toString()+"\t");   
	                }   
	            }   
	            context.write(new Text(sb.toString()),NullWritable.get());   
	            }   
	

}
