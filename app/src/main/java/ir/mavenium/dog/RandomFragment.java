package ir.mavenium.dog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class RandomFragment extends Fragment implements View.OnClickListener {

    private Button fetchButton;
    private ImageView dogimageView;
    private DogApiServices dogApiServices;
    private TextView dogBreedName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_random, container, false);
        fetchButton = rootView.findViewById(R.id.fragment_random_fetch_button);
        fetchButton.setOnClickListener(this);
        dogimageView = rootView.findViewById(R.id.fragment_random_dog_imageView);
        dogBreedName = rootView.findViewById(R.id.fragment_random_dog_breed_name);
        return rootView;
    }

    @Override
    public void onStart() {
        sendRequest();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_random_fetch_button) {
            sendRequest();
        }
    }

    private void sendRequest() {
        if(hasInternetConnection()){
            dogApiServices = new DogApiServices(RandomFragment.super.getContext());
            dogApiServices.getRandomImage(new DogApiServices.RandomResultCallBack() {
                @Override
                public void onRandomImageRecived(String message) {
                    Picasso.get().load(message).into(dogimageView);
                    String[] separated = message.split("/");
                    dogBreedName.setText(separated[4].trim());
                }

                @Override
                public void onRandomImageError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), getText(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }


}
