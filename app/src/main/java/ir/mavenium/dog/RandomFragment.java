package ir.mavenium.dog;

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
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_random_fetch_button) {
//            Toast.makeText(RandomFragment.super.getContext(), "Click", Toast.LENGTH_SHORT).show();
            dogApiServices = new DogApiServices(RandomFragment.super.getContext());
            dogApiServices.getRandomImage(new DogApiServices.RandomResultCallBack() {
                @Override
                public void onRandomImageRecived(String message) {
//                    Toast.makeText(RandomFragment.super.getContext(), message, Toast.LENGTH_SHORT).show();
                    Picasso.get().load(message).into(dogimageView);
                    String[] separated = message.split("/");
                    dogBreedName.setText(separated[4].trim());

                }

                @Override
                public void onRandomImageError(String error) {
                    Toast.makeText(RandomFragment.super.getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
