import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CreateJobOutput;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.JobInput;
import com.amazonaws.services.elastictranscoder.model.ReadJobRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tallen on 14/03/16.
 */
public class MioAwsTrancoder {

    private AWSCredentials credentials;


    public MioAwsTrancoder(final String accessKey, final String secretKey) {
        credentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return accessKey;
            }

            @Override
            public String getAWSSecretKey() {
                return secretKey;
            }
        };
    }

    public String createJob(String fileName, String outputFilename, String pipelineId){
        AmazonElasticTranscoder amazonElasticTranscoder = new AmazonElasticTranscoderClient(credentials);
        amazonElasticTranscoder.setRegion(Region.getRegion(Regions.EU_WEST_1));

        // Setup the job input using the provided input key.
        JobInput input = new JobInput().withKey(fileName);

        // Setup the job output using the provided input key to generate an output key.
        List<CreateJobOutput> outputs = new ArrayList<CreateJobOutput>();
        CreateJobOutput output = new CreateJobOutput()
                .withKey(outputFilename)
                .withPresetId("1351620000001-000020"); // This will generate a 480p 16:9 mp4 output.
        outputs.add(output);

        // Create a job on the specified pipeline and return the job ID.
        CreateJobRequest createJobRequest = new CreateJobRequest()
                .withPipelineId(pipelineId)
                .withInput(input)
                .withOutputs(outputs);

        return amazonElasticTranscoder.createJob(createJobRequest).getJob().getId();
    }

    public String getJobStatus(String jobId){

        AmazonElasticTranscoder amazonElasticTranscoder = new AmazonElasticTranscoderClient(credentials);
        amazonElasticTranscoder.setRegion(Region.getRegion(Regions.EU_WEST_1));

        ReadJobRequest jobRequest = new ReadJobRequest()
                .withId(jobId);

        return amazonElasticTranscoder.readJob(jobRequest).getJob().getStatus();
    }
}
