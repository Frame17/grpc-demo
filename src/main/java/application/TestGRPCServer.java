package application;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class TestGRPCServer {

    private final Server server = ServerBuilder
            .forPort(8080)
            .addService(new PingServiceImpl())
//            .addService(
//                    new PingServiceGrpcKt.PingServiceCoroutineImplBase() {
//                        @Nullable
//                        @Override
//                        public Object ping(@NotNull Request request, @NotNull Continuation<? super Response> $completion) {
//                            return Response.newBuilder().build();
//                        }
//                    }
//            )
            .build();

    public void start() throws IOException, InterruptedException {
        server.start();
        System.out.println("Server started");
        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final TestGRPCServer testGRPCServer = new TestGRPCServer();
        testGRPCServer.start();
    }

    static class PingServiceImpl extends PingServiceGrpc.PingServiceImplBase {

        @Override
        public void ping(Request request, StreamObserver<Response> responseObserver) {
            Response reply = Response.newBuilder().build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
