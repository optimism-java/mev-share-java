package event;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.disposables.Disposable;
import net.flashbots.MevShareClient;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.MevShareEvent;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * SSE subscribe Example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SseSubscribe {

    public static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .directory(Paths.get("", "example").toAbsolutePath().toString())
                .filename(".env")
                .load();
        Credentials authSigner = Credentials.create(dotenv.get("AUTH_PRIVATE_KEY"));
        Web3j web3j = Web3j.build(new HttpService(dotenv.get("PROVIDER_URL")));

        var mevShareClient = new MevShareClient(Network.GOERLI, authSigner, web3j);

        CountDownLatch latch = new CountDownLatch(5);
        List<MevShareEvent> events = new ArrayList<>();
        Consumer<MevShareEvent> eventListener = mevShareEvent -> {
            events.add(mevShareEvent);
            latch.countDown();
        };

        Disposable disposable = mevShareClient.subscribe(eventListener);

        latch.await();

        // remember to release when no longer to subscribe events
        disposable.dispose();

        System.out.println(events);
    }
}
