package ir.mavenium.dog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class RandomByBreedFragment extends Fragment {

    private DogApiServices dogApiServices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_random_by_breed, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        dogApiServices = new DogApiServices(RandomByBreedFragment.super.getContext());
        dogApiServices.getListAllBreeds(new DogApiServices.ListAllBreedsCallBack() {
            @Override
            public void onListAllBreedsRecived(List<String> Breeds) {

            }

            @Override
            public void onListAllBreedsError(String Error) {

            }
        });
        super.onStart();
    }
}
