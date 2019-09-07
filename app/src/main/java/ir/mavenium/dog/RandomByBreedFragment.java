package ir.mavenium.dog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomByBreedFragment extends Fragment implements View.OnClickListener {

    private Spinner listOfBreeds;
    private ImageView dogImageView;
    private Button fetchButton;
    private DogApiServices dogApiServices;
    private String breedsSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_random_by_breed, container, false);
        listOfBreeds = rootView.findViewById(R.id.fragment_random_by_breed_list_of_breeds);
        dogImageView = rootView.findViewById(R.id.fragment_random_by_breed_imageView);
        fetchButton = rootView.findViewById(R.id.fragment_random_by_breed_fetch_button);
        fetchButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart() {
        dogApiServices = new DogApiServices(RandomByBreedFragment.super.getContext());
        dogApiServices.getListAllBreeds(new DogApiServices.ListAllBreedsCallBack() {
            @Override
            public void onListAllBreedsRecived(List<String> Breeds) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Breeds);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listOfBreeds.setAdapter(spinnerAdapter);
                listOfBreeds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        breedsSelected = listOfBreeds.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        breedsSelected = "affenpinscher";
                    }
                });

            }

            @Override
            public void onListAllBreedsError(String Error) {
                Toast.makeText(getContext(), Error, Toast.LENGTH_SHORT).show();
            }
        });
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_random_by_breed_fetch_button) {
            sendRequest();
        }
    }

    public void sendRequest() {
        dogApiServices.getRandomImageByBreed(new DogApiServices.RandomByBreedResultCallBack() {
            @Override
            public void OnRandomImageByBreedRecived(String message) {
                Picasso.get().load(message).into(dogImageView);
            }

            @Override
            public void OnRandomImageByBreedError(String Error) {
                Toast.makeText(getContext(), Error, Toast.LENGTH_SHORT).show();
            }
        }, breedsSelected);
    }
}
