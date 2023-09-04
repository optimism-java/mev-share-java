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

        CountDownLatch latch1 = new CountDownLatch(5);
        List<MevShareEvent> txEvents = new ArrayList<>();
        Consumer<MevShareEvent> txEventListener = mevShareEvent -> {
            txEvents.add(mevShareEvent);
            latch1.countDown();
        };
        Disposable disposable1 = mevShareClient.subscribeTx(txEventListener);
        latch1.await();

        // remember to release when no longer to subscribe events
        disposable1.dispose();

        System.out.println(txEvents);

        mevShareClient.close();
        // subscribe bundle events
        // bundle events are very rare, so we comment it out

        //        CountDownLatch latch2 = new CountDownLatch(1);
        //        List<MevShareEvent> bundleEvents = new ArrayList<>();
        //        Consumer<MevShareEvent> bundleEventListener = mevShareEvent -> {
        //            bundleEvents.add(mevShareEvent);
        //            latch2.countDown();
        //        };
        //        Disposable disposable2 = mevShareClient.subscribeBundle(bundleEventListener);
        //        latch2.await();
        //
        //        // remember to release when no longer to subscribe events
        //        disposable2.dispose();
        //
        //        System.out.println(bundleEvents);
    }
}
