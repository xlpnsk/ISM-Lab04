package pollub.ism.lab04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button field=null,resetBtn=null;
    private int[] boardState = new int[9]; //tablica stanu planszy
    private static String KEY_BOARDSTATE = "Tablica stanow planszy", KEY_ROUND="Numer rundy", KEY_WIN="Stan rozgrywki";
    private int round; //nr rundy
    private boolean win; //czy gra zakonczona

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_BOARDSTATE, boardState);
        outState.putInt(KEY_ROUND, round);
        outState.putBoolean(KEY_WIN,win);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boardState = savedInstanceState.getIntArray(KEY_BOARDSTATE);
        round = savedInstanceState.getInt(KEY_ROUND);
        win=savedInstanceState.getBoolean(KEY_WIN);
        for(int i=0;i<9;i++)
        {
            String bname="square"+Integer.toString(i+1);
            int refId=getResources().getIdentifier(bname,"id",getPackageName());
            field = (Button) findViewById(refId);
            switch (boardState[i])
            {
                case 1:
                {
                    field.setText("O");
                    field.setClickable(false);
                    break;
                }
                case 2:
                {
                    field.setText("X");
                    field.setClickable(false);
                    break;
                }
                default:
                {
                    field.setText("");
                    break;
                }
            }
        }
        if(win)
        {
            koniec();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<9;i++)
            boardState[i]=0;
        round=0;
        resetBtn=findViewById(R.id.restart);
        resetBtn.setVisibility(View.INVISIBLE);
        win=false;
    }

    public void kliknieciePrzycisku(View view){
        int player=(round%2)+1;
        String nazwaPrzycisku = view.getResources().getResourceEntryName(view.getId());
        int num=Integer.parseInt(nazwaPrzycisku.substring(6));
        boardState[num-1]=player;
        field=(Button) findViewById(view.getId());
        String player_sign;
        if(player==1)
            player_sign="O";
        else
            player_sign="X";
        field.setText(player_sign);
        field.setClickable(false);
        if(czyWygrana(player_sign)) {
            koniec();
        }
        round++;
    }

    public boolean czyWygrana(String player_sign){
        for(int i=0,j=0;i<3;i++,j=j+3)
        {
            if (boardState[j] == boardState[j + 1] && boardState[j + 1] == boardState[j + 2] && boardState[j]>0) { //poziom
                win=true;
                break;
            } else if (boardState[i] == boardState[i + 3] && boardState[i + 3] == boardState[i + 6] && boardState[i]>0) { //pion
                win=true;
                break;
            }
        }
        if(boardState[4]>0) {
            if (boardState[0] == boardState[4] && boardState[4] == boardState[8]) //ukos
            {
                win=true;
            } else if (boardState[2] == boardState[4] && boardState[4] == boardState[6]) //ukos
            {
                win=true;
            }
        }
        if(win)
        {
            Toast.makeText(this, player_sign+" jest zwycięzcą!", Toast.LENGTH_LONG).show();
        }
        else if(round==8) {
            Toast.makeText(this,"Remis!", Toast.LENGTH_LONG).show();
            win=true;
        }
        return win;
    }

    void koniec()
    {
        resetBtn.setVisibility(View.VISIBLE);
        for(int i=0;i<9;i++) {
            String bname = "square" + Integer.toString(i + 1);
            int refId = getResources().getIdentifier(bname, "id", getPackageName());
            field = (Button) findViewById(refId);
            field.setClickable(false);
        }
    }

    public void restart(View view)
    {
        for(int i=0;i<9;i++)
            boardState[i]=0;
        round=0;
        win=false;
        resetBtn=findViewById(R.id.restart);
        resetBtn.setVisibility(View.INVISIBLE);
        for(int i=0;i<9;i++) {
            String bname = "square" + Integer.toString(i + 1);
            int refId = getResources().getIdentifier(bname, "id", getPackageName());
            field = (Button) findViewById(refId);
            field.setText("");
            field.setClickable(true);
        }
    }
}