package event;

import java.util.function.Consumer;

import io.reactivex.disposables.Disposable;
import net.flashbots.MevShareClient;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.MevShareEvent;

/**
 * SSE subscribe Example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SseSubscribe {

    public static void main(String[] args) {
        var mevShareClient = new MevShareClient(Network.GOERLI, null, null);
        Consumer<MevShareEvent> eventListener = mevShareEvent -> {
            // do something and do not block here...
        };

        Disposable disposable = mevShareClient.subscribe(eventListener);

        // remember to release when no longer to subscribe events
        disposable.dispose();
    }
}
