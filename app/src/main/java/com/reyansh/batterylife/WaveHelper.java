package com.reyansh.batterylife;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

public class WaveHelper {
    private WaveView mWaveView;
    private float mPercentage = 0.0f;
    private AnimatorSet mAnimatorSet;

    public void setPercentage(float percentage) {
        mPercentage = percentage;
        initAnimation();
    }


    public WaveHelper(WaveView waveView) {
        mWaveView = waveView;
        initAnimation();
    }

    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(5000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", 0f, mPercentage);
        waterLevelAnim.setDuration(3000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }


    public void cancel() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
    }
}