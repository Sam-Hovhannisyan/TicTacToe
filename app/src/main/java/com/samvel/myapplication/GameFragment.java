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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class GameFragment extends Fragment implements View.OnClickListener {

    GridLayout gridlayout;
    int n = 9, count, mode, number;
    int[] game;
    boolean[] isClickable;
    boolean f, cModeFlag = false;
    int width, height;
    LinearLayout ptext, l1, l2;
    EditText p1Edit, p2Edit;
    TextView ptext1, ptext2, endText, vsComputer, twoPlayer, header;
    Button exit, playAgain, playMul, easy, medium, hard;
    AlertDialog endDialog, twoPlayerDialog, cModeDialog;
    ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

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
        View mulAlert = getLayoutInflater().inflate(R.layout.two_player_alert, null);
        View cMode = getLayoutInflater().inflate(R.layout.game_alert, null);

        endText = theEnd.findViewById(R.id.endText);
        exit = theEnd.findViewById(R.id.exit);
        playAgain = theEnd.findViewById(R.id.playAgain);

        builder.setView(theEnd);
        endDialog = builder.create();
        endDialog.setCancelable(false);

        builder.setView(mulAlert);
        twoPlayerDialog = builder.create();
        twoPlayerDialog.setCancelable(true);

        builder.setView(cMode);
        cModeDialog = builder.create();
        cModeDialog.setCancelable(true);

        gridlayout = view.findViewById(R.id.gridlayout);
        vsComputer = view.findViewById(R.id.vsComputer);
        twoPlayer = view.findViewById(R.id.twoPlayer);
        header = view.findViewById(R.id.header);

        game = new int[n];
        isClickable = new boolean[n];

        // Multiplayer dialog settings

        p1Edit = mulAlert.findViewById(R.id.player1);
        p2Edit = mulAlert.findViewById(R.id.player2);

        playMul = mulAlert.findViewById(R.id.startGame);

        l1 = mulAlert.findViewById(R.id.linearLayout2);
        l2 = mulAlert.findViewById(R.id.linearLayout3);

        // Computer mode dialog settings

        easy = cMode.findViewById(R.id.easy);
        medium = cMode.findViewById(R.id.medium);
        hard = cMode.findViewById(R.id.hard);

        // Default settings

        ptext = view.findViewById(R.id.ptext);
        ptext1 = view.findViewById(R.id.ptext1);
        ptext2 = view.findViewById(R.id.ptext2);

        ptext.setVisibility(View.INVISIBLE);

        easy.setOnClickListener(v -> {
            mode = 1;
            cModeFlag = true;
            cModeDialog.cancel();
            generate();
            vsComputer.setVisibility(View.INVISIBLE);
            twoPlayer.setVisibility(View.INVISIBLE);
            header.setText("Easy Mode");
        });
        medium.setOnClickListener(v -> {
            mode = 2;
            cModeFlag = true;
            cModeDialog.cancel();
        });
        hard.setOnClickListener(v -> {
            mode = 3;
            cModeFlag = true;
            cModeDialog.cancel();
        });

        vsComputer.setOnClickListener(v -> {
            vsComputer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce));
            cModeDialog.show();
        });

        twoPlayer.setOnClickListener(v -> {
            twoPlayer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce));
            twoPlayerDialog.show();
        });

        playMul.setOnClickListener(v -> {
            startGame();
            twoPlayerDialog.cancel();
            vsComputer.setVisibility(View.INVISIBLE);
            twoPlayer.setVisibility(View.INVISIBLE);
        });

        exit.setOnClickListener(v -> {
            endDialog.cancel();
            cModeFlag = false;
            header.setText("Choose Game Mode");
            ptext.setVisibility(View.INVISIBLE);
            vsComputer.setVisibility(View.VISIBLE);
            twoPlayer.setVisibility(View.VISIBLE);
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
        if (!cModeFlag) {
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
        else {
            if (mode == 1){
                view.setBackgroundResource(R.drawable.x);
                game[id] = 0;
                isClickable[id] = false;
                numbers.remove((Integer) id);
                if (numbers.size() == 0){
                    System.out.println(numbers);
                    for (int i = 0; i < n; i++) {
                        if (game[i] != -1) {
                            if (i % 3 == 0) {
                                if (game[i] == game[i + 1] && game[i + 1] == game[i + 2]) {
                                    print(game[i]+3);
                                    return;
                                }
                            }
                            if (i == 0 || i == 1 || i == 2) {
                                if (game[i] == game[i + 3] && game[i + 3] == game[i + 6]) {
                                    print(game[i]+3);
                                    return;
                                }
                            }
                            if (i == 0 && game[0] == game[4] && game[4] == game[8]) {
                                print(game[i]+3);
                                return;
                            }
                            if (i == 2 && game[2] == game[4] && game[4] == game[6]) {
                                print(game[i]+3);
                                return;
                            }
                        }
                    }
                    print(2);
                    return;
                }
                int index = (int)(Math.random() * numbers.size());
                number = numbers.get(index);
                game[number] = 1;
                isClickable[number] = false;
                gridlayout.getChildAt(number).setBackgroundResource(R.drawable.o);
                numbers.remove((Integer) number);
                System.out.println(number);
                System.out.println(numbers);
                for (int i = 0; i < n; i++) {
                    if (game[i] != -1) {
                        if (i % 3 == 0) {
                            if (game[i] == game[i + 1] && game[i + 1] == game[i + 2]) {
                                print(game[i]+3);
                                break;
                            }
                        }
                        if (i == 0 || i == 1 || i == 2) {
                            if (game[i] == game[i + 3] && game[i + 3] == game[i + 6]) {
                                print(game[i]+3);
                                break;
                            }
                        }
                        if (i == 0 && game[0] == game[4] && game[4] == game[8]) {
                            print(game[i]+3);
                            break;
                        }
                        if (i == 2 && game[2] == game[4] && game[4] == game[6]) {
                            print(game[i]+3);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void print(int i) {
        if (i == 0) endText.setText(p1Edit.getText() + " WIN!");
        else if (i == 1) endText.setText(p2Edit.getText() + " WIN!");
        else if (i == 2) endText.setText("Draw!");
        else if (i == 3) endText.setText("You Win!");
        else if (i == 4) endText.setText("You Lose!");
        fillClickable();
        endDialog.show();
        numbers = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
    }

    private void fillClickable() {
        for (int i = 0; i < n; i++) {
            isClickable[i] = false;
        }
    }

    private void startGame() {
        generate();
        header.setText("2 Player Mode");
        ptext1.setTextColor(Color.RED);
        ptext2.setTextColor(Color.rgb(226, 209, 166));
        ptext1.setText(p1Edit.getText());
        ptext2.setText(p2Edit.getText());
        ptext.setVisibility(View.VISIBLE);
    }
}