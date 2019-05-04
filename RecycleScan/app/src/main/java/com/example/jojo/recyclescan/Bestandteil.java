package com.example.jojo.recyclescan;

public class Bestandteil {
        private int mImageDrawable;
        private String mName;

        // Constructor that is used to create an instance of the Movie object
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
