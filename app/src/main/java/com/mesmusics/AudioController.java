package com.mesmusics;

import android.content.Context;
import android.view.View;
import android.widget.MediaController;

public class AudioController extends MediaController {

    public AudioController(Context context){
        super(context);
    }

    public void hide(){}

  /*  controller.setPrevNextListeners(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playNext();
        }
    }, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playPrev();
        }
    });*/

}

