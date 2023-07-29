package com.example.wordlecheater;

import android.view.View;
import android.widget.Button;
import androidx.core.content.res.ResourcesCompat;

public class TileButton implements View.OnClickListener {

    public TileButton(Button button){
        this.button = button;
        this.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    enum TileStyle{
        EMPTY, WHITE, GRAY, YELLOW, GREEN
    }

    private TileStyle style;
    public Button button;

    /**
     * Sets the style of this to the given style
     * @param style the style that this button should have
     */
    public void setStyle(TileStyle style) {
        this.style = style;
        switch (style){
            case GRAY:
                button.setTextColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.text_white, null));
                button.setBackgroundColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.select_gray, null));
            case YELLOW:
                button.setTextColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.text_white, null));
                button.setBackgroundColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.select_yellow, null));
            case GREEN:
                button.setTextColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.text_white, null));
                button.setBackgroundColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.select_green, null));
            case WHITE:
                button.setTextColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.text_black, null));
                button.setBackgroundResource(R.drawable.white_tile_background);
            case EMPTY:
                button.setTextColor(ResourcesCompat.getColor(button.getContext().getResources(), R.color.text_white, null));
                button.setBackgroundResource(R.drawable.empty_tile_background);
        }
    }

    public void lock(){
        button.setClickable(false);
    }

    public void unlock(){
        button.setClickable(true);
    }
}
