package com.example.jojo.helpers;

/**
 * Klasse Bestandteil für MyAdapter. Ein Bestandteil in der ListView besteht aus einem Namen und einem Bild.
 */
public class Bestandteil {
        private int mImageDrawable;
        private String mName;


        public Bestandteil(int mImageDrawable, String mName) {
            this.mImageDrawable = mImageDrawable;
            this.mName = mName;
        }

        public int getmImageDrawable() {
            return mImageDrawable;
        }

        public void setmImageDrawable(int mImageDrawable) {
            this.mImageDrawable = mImageDrawable;
        }

        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }


}
