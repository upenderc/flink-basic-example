package com.uppi.poc.flink;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class UpperCaseTransformationApp {

	public static void main(String[] args) throws Exception {
		DataStream<String> dataStream=null;
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		final ParameterTool params=ParameterTool.fromArgs(args);
		env.getConfig().setGlobalJobParameters(params);
		if (params.has("input") && params.has("output")) {
			//data source
			dataStream=env.readTextFile(params.get("input"));
		} else {
			System.err.println("No input specified. Please run 'UpperCaseTransformationApp --input <file-to-path> --output <file-to-path>'");
            return;
		}
		if (dataStream==null) {
			System.err.println("DataStream created as null, check file path");
			System.exit(1);
			return;
		}
		//transformation
		SingleOutputStreamOperator<String> soso=dataStream.map(String::toUpperCase);
		//data sink
		soso.writeAsText(params.get("output"),WriteMode.OVERWRITE);
		env.execute("read and write");
	}

}
