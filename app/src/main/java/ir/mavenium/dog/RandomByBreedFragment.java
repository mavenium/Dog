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

import java.util.List;

public class RandomByBreedFragment extends Fragment {

    private Spinner listOfBreeds;
    private ImageView dogImageView;
    private Button fetchButton;
    private DogApiServices dogApiServices;

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
                        Toast.makeText(getContext(), listOfBreeds.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

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
}
