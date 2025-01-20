package com.example.parking.ui.parking;

import static com.example.parking.ui.parking.ParkingSimulatorFragment.slots;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.parking.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import commons.entities.Park;

public class BlockHandler {

    private ImageView entryBlock, exitBlock;
    private TextView entryStatus, exitStatus;
    private final FragmentActivity activity;
    private final View view;
    private ViewGroup parentLayout;
    private final Random random;

    private final List<ParkingSlot> avblSpots = new ArrayList<>();
    private final List<ParkingSlot> takenSpots = new ArrayList<>();

    public BlockHandler(FragmentActivity activity, View view){
        this.activity = activity;
        this.view = view;

        this.random = new Random();

    }

    public void initVariables(Park park){
        entryBlock = view.findViewById(R.id.enter_block);
        exitBlock = view.findViewById(R.id.exit_block);
        entryStatus = view.findViewById(R.id.entryStatus);
        exitStatus = view.findViewById(R.id.exitStatus);
        parentLayout = view.findViewById(R.id.mainFrame);

        for (ParkingSlot ps : avblSpots)
            avblSpots.remove(ps);

        for (ParkingSlot ps : takenSpots)
            takenSpots.remove(ps);

        for (int i = 0; i < park.getSlots().length; i++)
            for (int j = 0; j < park.getSlots()[0].length; j++){
                slots[i][j].setVisibility(TextView.VISIBLE);
                avblSpots.add(new ParkingSlot(slots[i][j], i, j));
            }
        ParkingSimulatorFragment.canAcceptNewCar = true;
    }

    public void start(ImageView car){
        activity.runOnUiThread(()->{
            entryBlock.setImageResource(R.drawable.car_block_closed);
        });
        entryBlock.postDelayed(()->{
            entryBlock.setImageResource(R.drawable.car_block_yellow);
            handleYellowEntryBlock(car);
        }, 2000);
    }

    private void handleYellowEntryBlock(ImageView car){
        String status = "Processing...";
        activity.runOnUiThread(()->{
            entryStatus.setText(status);
            entryStatus.setTextColor(Color.parseColor("#ebc334"));
            entryStatus.setVisibility(View.VISIBLE);
        });

     entryBlock.postDelayed(()->{
         String msg;
         if (!avblSpots.isEmpty()){
             msg = "Creating ticket..";
             entryStatus.setText(msg);

             entryBlock.postDelayed(()->{
                 carEnter(car);
             }, 2000);

         }else{
             msg = "Park is full";
             entryStatus.setText(msg);
             entryStatus.setTextColor(Color.parseColor("#8c0808"));

             entryBlock.postDelayed(()->{
                activity.runOnUiThread(()->{
                    entryBlock.setImageResource(R.drawable.car_block_closed);
                    parentLayout.removeView(car);
                    entryStatus.setVisibility(View.INVISIBLE);
                    ParkingSimulatorFragment.canAcceptNewCar = true;
                });
            }, 2000);
         }
     }, 1500);

    }

    private void carEnter(ImageView car){
        activity.runOnUiThread(()->{
            String txt = "Calculating path..";
            entryStatus.setText(txt);
        });

        entryBlock.postDelayed(()->{
            int roll = random.nextInt(avblSpots.size());
            ParkingSlot ps = avblSpots.get(roll);
            avblSpots.remove(ps);

            activity.runOnUiThread(()->{
                ps.getImg().setImageResource(R.drawable.parking_spot_border_yellow);
                moveCar(ps, car);
            });
        }, 2000);
    }

    private void moveCar(ParkingSlot toSlot, ImageView car){
        activity.runOnUiThread(()->{
            entryBlock.setImageResource(R.drawable.car_block_open);
            String msg = "Happy Parking!";
            entryStatus.setText(msg);
            entryStatus.setTextColor(Color.parseColor("#0da329"));
        });

        entryStatus.postDelayed(()->{
            ParkingSimulatorFragment.canAcceptNewCar = true;
            entryStatus.setVisibility(View.INVISIBLE);
        }, 2000);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int currentMinutes = calendar.get(Calendar.MINUTE);

        toSlot.setEntryHour(currentHour);
        toSlot.setEntryMin(currentMinutes);
        toSlot.setTimeOfEntry(System.currentTimeMillis());

        if (toSlot.getRow() > 1){
            positionToY(toSlot, car);
        }else{
            moveOnX(toSlot, car);
        }
    }

    private void positionToY(ParkingSlot slot, ImageView car){
        activity.runOnUiThread(() -> {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(car, "x", car.getX(), entryBlock.getX() - 50);
            animatorX.setDuration(2000);

            animatorX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    car.postDelayed(()->{
                        car.setImageResource(R.drawable.car_going_down);
                        entryBlock.setImageResource(R.drawable.car_block_closed);
                        moveOnY(slot, car);

                    }, 1500);
                }
            });
            animatorX.start();
        });
    }

    private void moveOnX(ParkingSlot slot, ImageView car){
        ImageView arrow = new ImageView(view.getContext());
        activity.runOnUiThread(() -> {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(car, "x", car.getX(), slot.getImg().getX());
            animatorX.setDuration((slot.getCol() + 1) * 1000L);
            animatorX.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation){
                    if (slot.getRow() <= 1)
                        car.postDelayed(()->{
                            entryBlock.setImageResource(R.drawable.car_block_closed);
                        }, 1500);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    parentLayout.postDelayed(()->{
                        parentLayout.removeView(car);
                        slot.getImg().setImageResource(R.drawable.car_going_down);
                        slot.getImg().setBackgroundResource(R.drawable.parking_spot_border_red);
                        takenSpots.add(slot);
                    }, 1500);
                }
            });

            arrow.setX((car.getX()) + 150);
            arrow.setY(car.getY());
            arrow.setImageResource(R.drawable.right_arrow);
            arrow.setVisibility(View.VISIBLE);
            parentLayout.addView(arrow);

            ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(arrow, "x", car.getX() + 150, slot.getImg().getX());
            animatorX2.setDuration((slot.getCol() + 1) * 800L);

            animatorX2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    parentLayout.removeView(arrow);
                }
            });

            animatorX2.start();
            animatorX.start();

        });
    }

    private void moveOnY(ParkingSlot slot, ImageView car){
        float yDest;
        ImageView arr_right2 = view.findViewById(R.id.arrow_right_2);
        ImageView arr_right3 = view.findViewById(R.id.arrow_right_3);

        if (slot.getRow() == 2)
            yDest = arr_right2.getY();
        else
            yDest = arr_right3.getY();

        ImageView arrow = new ImageView(view.getContext());

        activity.runOnUiThread(() -> {
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(car, "y", car.getY(), yDest);
            animatorY.setDuration((slot.getCol() + 1) * 1500L);

            animatorY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    car.postDelayed(()->{
                        car.setImageResource(R.drawable.car);
                        moveOnX(slot, car);
                    }, 1500);
                }
            });

            arrow.setX(car.getX());
            arrow.setY(car.getY() + 150);
            arrow.setImageResource(R.drawable.arrow_down);
            arrow.setVisibility(View.VISIBLE);
            parentLayout.addView(arrow);

            ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(arrow, "y", car.getY() + 150, yDest);
            animatorY2.setDuration((slot.getCol() + 1) * 1000L);

            animatorY2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    parentLayout.removeView(arrow);
                }
            });
            animatorY2.start();
            animatorY.start();
        });
    }

    public void handleCarExit(){
        if (!takenSpots.isEmpty()){
            int carToLeave = random.nextInt(takenSpots.size());
            ParkingSlot ps = takenSpots.get(carToLeave);

            if (System.currentTimeMillis() - ps.getTimeOfEntry() > 10000)
                startCarExit(ps);
        }
    }

    public void startCarExit(ParkingSlot slot){
        if (slot == null)
            return;
        new Thread(()->{
            takenSpots.remove(slot);
            avblSpots.add(slot);
            activity.runOnUiThread(()->{
                slot.getImg().setImageResource(0);
                slot.getImg().setBackgroundResource(R.drawable.parking_spot_border);

                ImageView exitingCar = new ImageView(view.getContext());
                exitingCar.setImageResource(R.drawable.car);

                exitingCar.setX(slot.getImg().getX());

                if (slot.getRow() == 0 || slot.getRow() == 1)
                    exitingCar.setY(ParkData.LANE_1_Y);
                else if (slot.getRow() == 2)
                    exitingCar.setY(ParkData.LANE_2_Y);
                else
                    exitingCar.setY(ParkData.LANE_3_Y);

                exitingCar.setVisibility(View.VISIBLE);
                parentLayout.addView(exitingCar);

                ObjectAnimator animatorX = ObjectAnimator.ofFloat(exitingCar, "x", exitingCar.getX(), slots[0][6].getX() + 100);
                animatorX.setDuration((6 - slot.getCol() + 1) * 1000L);

                animatorX.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Delay the next action without blocking the main thread
                        exitingCar.postDelayed(() -> {
                            carMoveToExit(slot, exitingCar, parentLayout);
                            exitingCar.setImageResource(R.drawable.car_going_down);
                        }, 1500); // 1.5-second delay

                    }
                });
                animatorX.start();
            });
        }).start();
    }

    private void carMoveToExit(ParkingSlot slot, ImageView exitingCar, ViewGroup parent){
        if (exitingCar == null)
            return;
        activity.runOnUiThread(()->{
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(exitingCar, "y", exitingCar.getY(), exitBlock.getY() + 30);
            animatorY.setDuration((slot.getRow() + 1) * 1000L);
            animatorY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Change to yellow block
                    exitBlock.setImageResource(R.drawable.car_block_yellow);
                    String msg = "Reading ticket";
                    exitStatus.setTextColor(Color.parseColor("#ebc334"));
                    exitStatus.setText(msg);
                    exitStatus.setVisibility(View.VISIBLE);
                    exitBlock.postDelayed(()->{
                        String msg2 = "Processing payment";
                        exitStatus.setText(msg2);
                        // Delay using postDelayed instead of Thread.sleep
                        exitBlock.postDelayed(() -> {
                            exitBlock.setImageResource(R.drawable.car_block_open);
                            @SuppressLint("DefaultLocale")
                            String msg3 = String.format("%.2f$, Payment success", slot.getCost());
                            exitStatus.setTextColor(Color.parseColor("#0da329"));
                            exitStatus.setText(msg3);

                            // Delay to close the block and remove the car
                            exitBlock.postDelayed(() -> {
                                exitBlock.setImageResource(R.drawable.car_block_closed);
                                exitStatus.setVisibility(View.INVISIBLE);
                                parent.removeView(exitingCar);
                                slot.setTimeOfEntry(-1);
                            }, 1500);
                        }, 2500);
                    }, 1500);
                }
            });
            animatorY.start();
        });
    }
}
