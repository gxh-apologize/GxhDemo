package cn.gxh.property.message;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.TransportEnum;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/4/17 10:12
 */
public class MessageFragment extends BaseFragment {

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    String url;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        Button btnMessage = findView(R.id.btn_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hubConnection.send("Send", "hhhhhhhhh");
                hubConnection.stop();
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

//        url="http://signalr-samples.azurewebsites.net/default";
        url="http://newretail.shundaonetwork.com/lololink";

        signalr();
    }

    HubConnection hubConnection;
    public void signalr() {
        hubConnection = HubConnectionBuilder.create(url)
                .withTransport(TransportEnum.LONG_POLLING)
                .build();

        hubConnection.on("SendAsync", (message) -> {
            Logger.d("gxh",message+"#haha");
        }, String.class);
        new HubConnectionTask().execute(hubConnection);
    }

    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            hubConnection.start().blockingAwait();
            Logger.d("gxh",hubConnection.getConnectionState().toString());
            return null;
        }
    }
}
