package com.example.worktracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuoteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quote, container, false);

        TextView textViewQuote = view.findViewById(R.id.textViewQuote);
        loadQuote(textViewQuote);

        return view;
    }

    private void loadQuote(TextView textViewQuote) {
        QuoteApi quoteApi = RetrofitClient.getClient().create(QuoteApi.class);

        quoteApi.getRandomQuote().enqueue(new Callback<List<QuoteResponse>>() {
            @Override
            public void onResponse(Call<List<QuoteResponse>> call,
                                   Response<List<QuoteResponse>> response) {
                if (response.isSuccessful() && response.body() != null
                        && !response.body().isEmpty()) {
                    QuoteResponse quote = response.body().get(0);
                    textViewQuote.setText("\"" + quote.getQ() + "\"\n- " + quote.getA());
                } else {
                    textViewQuote.setText("Stay focused and keep working hard.");
                }
            }

            @Override
            public void onFailure(Call<List<QuoteResponse>> call, Throwable t) {
                textViewQuote.setText("Stay focused and keep working hard.");
            }
        });
    }
}