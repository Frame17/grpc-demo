package application

import io.grpc.Server
import io.grpc.ServerBuilder

class TestServer {
    val server: Server = ServerBuilder
        .forPort(8080)
        .addService(object : PingServiceGrpcKt.PingServiceCoroutineImplBase() {
            override suspend fun ping(request: Request): Response = Response
                .newBuilder()
                .build()
        })
        .build()

    fun start() {
        server.start()
        println("Server started, listening on 8080")
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    val server = TestServer()
    server.start()
    server.blockUntilShutdown()
}


