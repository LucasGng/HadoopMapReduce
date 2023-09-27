package advanced.customwritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;

public class HigherCommodityperYear {

    // Job creation and IO setting
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        Configuration c = new Configuration();
        String[] files = new GenericOptionsParser(c, args).getRemainingArgs();

        Path inputFile = new Path(files[0]); // Input file
        Path outputFile = new Path(files[1]); // Output file

        // Jobs creation (with its names)
        Job j1 = Job.getInstance(c, "commodityTrade");

        // Setting Jobs classes
        j1.setJarByClass(HigherCommodityperYear.class);
        j1.setMapperClass(MapForCommodityYear.class);
        j1.setReducerClass(ReduceForCommodityYear.class);

        // Setting Job output classes
        j1.setMapOutputKeyClass(TypeYear.class);
        j1.setMapOutputValueClass(CommodityTrade.class);
        j1.setOutputKeyClass(TypeYear.class);
        j1.setOutputKeyClass(CommodityTrade.class);


        // Registering IO files
        FileInputFormat.addInputPath(j1, inputFile);
        FileOutputFormat.setOutputPath(j1, outputFile);

        // Launching the jobs and awaiting theirs executions
        System.exit(j1.waitForCompletion(true)  ? 0 : 1);
    }

    public static class MapForCommodityYear extends Mapper<LongWritable, Text, TypeYear, CommodityTrade> {

        // Map function
        public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException {

            String[] columns = value.toString().split(";");
            String commodity = columns[3];
            String type = columns[7];
            String year = columns[1];


            try { // Filters any non-numeric value in the amount and year column
                Long trade = Long.parseLong(columns[5]);

                // Returning the pair key-value as (TypeYear(type, year), CommodityTrade(commodity, trade.longValue()))
                con.write(new TypeYear(type, year), new CommodityTrade(commodity, trade.longValue()));
            } catch (NumberFormatException e) {
                System.out.println("Non-numeric amount found.");
            }
        }

    }

    public static class ReduceForCommodityYear extends Reducer<TypeYear, CommodityTrade, TypeYear, CommodityTrade> {

        // Reduce function
        public void reduce(TypeYear key, Iterable<CommodityTrade> values, Context con) throws IOException, InterruptedException {
            // Adding all values from one key
            long totalTrade = 0;
            String Commodity = "";
            for (CommodityTrade value : values) {
                totalTrade += value.getTrade();
                Commodity = value.getCommodity();
            }

            // Returning the pair key-value as key, CommodityTrade(Commodity, totalTrade))

            con.write(key, new CommodityTrade(Commodity, totalTrade));
        }

    }


}
