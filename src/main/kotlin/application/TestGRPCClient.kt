package application

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.system.measureNanoTime

class TestClient(private val channel: ManagedChannel) {
    private val stub: PingServiceGrpcKt.PingServiceCoroutineStub = PingServiceGrpcKt.PingServiceCoroutineStub(channel)

    suspend fun ping() {
        val request = Request.newBuilder().build()
        stub.ping(request)
    }
}

fun main() {
    val port = 8080
    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
    val client = TestClient(channel)

    measureNanoTime {
        runBlocking {
            client.ping()
        }
    }.also {
        println("Connecting: $it")
    }

    measureNanoTime {
        runBlocking {
            client.ping()
        }
    }.also {
        println("Before warm up: $it")
    }

    val n = 100_000
    measureNanoTime {
        runBlocking {
            IntRange(0, n).map {
                async {
                    client.ping()
                }
            }.awaitAll()
        }
    }.also {
        println("Warm up: ${it / n}")
    }

    measureNanoTime {
        runBlocking {
            client.ping()
        }
    }.also {
        println("After warm up: $it")
    }
}