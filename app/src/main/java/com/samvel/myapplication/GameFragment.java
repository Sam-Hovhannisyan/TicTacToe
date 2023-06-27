package com.samvel.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameFragment extends Fragment implements View.OnClickListener {

    GridLayout gridlayout;
    int n = 9, count;
    int[] game;
    boolean[] isClickable;
    boolean f;
    int width, height;
    LinearLayout ptext, pl1, pl2;
    EditText pe1, pe2;
    TextView ptext1, ptext2, endText;
    Button start, exit, playAgain;
    AlertDialog endDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View theEnd = getLayoutInflater().inflate(R.layout.end_alert, null);

        endText = theEnd.findViewById(R.id.endText);
        exit = theEnd.findViewById(R.id.exit);
        playAgain = theEnd.findViewById(R.id.playAgain);

        builder.setView(theEnd);
        endDialog = builder.create();

        gridlayout = view.findViewById(R.id.gridlayout);

        int btn_color = Color.rgb(226, 209, 166);

        game = new int[n];
        isClickable = new boolean[n];

        start = view.findViewById(R.id.startBtn);
        start.setBackgroundColor(btn_color);

        ptext = view.findViewById(R.id.ptext);
        ptext1 = view.findViewById(R.id.ptext1);
        ptext2 = view.findViewById(R.id.ptext2);
        pl1 = view.findViewById(R.id.pl1);
        pl2 = view.findViewById(R.id.pl2);
        pe1 = view.findViewById(R.id.pe1);
        pe2 = view.findViewById(R.id.pe2);

        ptext.setVisibility(View.INVISIBLE);

        start.setOnClickListener(v -> startGame());

        exit.setOnClickListener(v -> {
            endDialog.cancel();
            ptext.setVisibility(View.INVISIBLE);
            pl1.setVisibility(View.VISIBLE);
            pl2.setVisibility(View.VISIBLE);
            pe1.setText("");
            pe2.setText("");
            start.setVisibility(View.VISIBLE);
            gridlayout.removeAllViews();
        });

        playAgain.setOnClickListener(v -> {
            startGame();
            endDialog.cancel();
        });

        return view;
    }

    private void generate() {

        int wdth = (int) Math.sqrt(n);
        int textColor = Color.rgb(226, 209, 166);
        count = 0;
        f = true;
        ptext1.setTextColor(Color.RED);
        ptext2.setTextColor(textColor);

        gridlayout.removeAllViews();
        gridlayout.setColumnCount(wdth);
        gridlayout.setRowCount(wdth);

        for (int i = 0; i < n; i++) {
            game[i] = -1;
            isClickable[i] = true;
        }

        for (int i = 0, c = 0, r = 0; i < n; i++, c++) {
            if (c == wdth) {
                c = 0;
                r++;
            }
            Button oButton = new Button(getActivity());
            oButton.setId(i);
            oButton.setBackgroundResource(R.drawable.btnbg);
            oButton.setOnClickListener(this);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = height / (n);
            param.width = height / (n);
            param.rightMargin = 20;
            param.topMargin = 20;
            param.bottomMargin = 20;
            param.leftMargin = 20;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            oButton.setLayoutParams(param);
            gridlayout.addView(oButton);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        int textColor = Color.rgb(226, 209, 166);
        if (!isClickable[id]) return;
        count++;

        if (f) {
            view.setBackgroundResource(R.drawable.x);
            ptext1.setTextColor(textColor);
            ptext2.setTextColor(Color.RED);
            game[id] = 0;
            isClickable[id] = false;
            f = false;
        } else {
            view.setBackgroundResource(R.drawable.o);
            ptext1.setTextColor(Color.RED);
            ptext2.setTextColor(textColor);
            game[id] = 1;
            isClickable[id] = false;
            f = true;
        }
        if (count == n) {
            print(2);
        }
        for (int i = 0; i < n; i++) {
            if (game[i] != -1) {
                if (i % 3 == 0) {
                    if (game[i] == game[i + 1] && game[i + 1] == game[i + 2]) {
                        print(game[i]);
                        break;
                    }
                }
                if (i == 0 || i == 1 || i == 2) {
                    if (game[i] == game[i + 3] && game[i + 3] == game[i + 6]) {
                        print(game[i]);
                        break;
                    }
                }
                if (i == 0 && game[0] == game[4] && game[4] == game[8]) {
                    print(game[i]);
                    break;
                }
                if (i == 2 && game[2] == game[4] && game[4] == game[6]) {
                    print(game[i]);
                    break;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void print(int i) {
        if (i == 0) endText.setText(pe1.getText() + " WIN!");
        else if (i == 1) endText.setText(pe2.getText() + " WIN!");
        else if (i == 2) endText.setText("Draw!");
        fillClickable();
        endDialog.show();
    }

    private void fillClickable() {
        for (int i = 0; i < n; i++) {
            isClickable[i] = false;
        }
    }

    private void startGame() {
        generate();
        ptext1.setTextColor(Color.RED);
        ptext2.setTextColor(Color.rgb(226, 209, 166));
        ptext1.setText(pe1.getText());
        ptext2.setText(pe2.getText());
        ptext.setVisibility(View.VISIBLE);
        pl1.setVisibility(View.INVISIBLE);
        pl2.setVisibility(View.INVISIBLE);
        start.setVisibility(View.INVISIBLE);
    }
}