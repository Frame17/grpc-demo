package application;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kotlin.ranges.IntRange;

import java.util.stream.StreamSupport;

import static application.PingServiceGrpc.newBlockingStub;


public class TestGRPCClient {
    private final PingServiceGrpc.PingServiceBlockingStub stub;

    public TestGRPCClient(ManagedChannel channel) {
        this.stub = newBlockingStub(channel);
    }

    public void ping() {
        final Request request = Request.newBuilder().build();
        stub.ping(request);
    }

    public static void main(String[] args) {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        final TestGRPCClient testGRPCClient = new TestGRPCClient(channel);

        long start = System.nanoTime();
        testGRPCClient.ping();
        long end = System.nanoTime();
        System.out.println("Connection: " + (end - start));

        start = System.nanoTime();
        testGRPCClient.ping();
        end = System.nanoTime();
        System.out.println("Before warm up: " + (end - start)) ;

        int n = 100_000;
        start = System.nanoTime();
        StreamSupport.stream(new IntRange(0, n).spliterator(), false)
                .forEach(integer -> testGRPCClient.ping());
        end = System.nanoTime();
        System.out.println("Warm up: " + (end - start) / n);

        start = System.nanoTime();
        testGRPCClient.ping();
        end = System.nanoTime();
        System.out.println("After warm up: " + (end - start));
    }
}
