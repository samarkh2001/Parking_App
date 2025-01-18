package com.example.parking.ui.parking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.parking.R;
import com.example.parking.client.Client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import commons.entities.Park;
import commons.requests.Message;
import commons.requests.RequestType;


public class ParkingSimulatorFragment extends Fragment {

    public static Park park;
    public static boolean reserveSuccess = false;
    private static ImageView[][] slots;
    private final List<ParkingSlot> avblSpots = new ArrayList<>();
    private final List<ParkingSlot> takenSpots = new ArrayList<>();
    private ImageView arr_right2, arr_right3;
    private ImageView entryBlock, exitBlock;
    private ViewGroup parentLayout;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    private TextView entryStatus, exitStatus;
    private boolean canAcceptNewCar = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_simulator_layout, container, false);
        TextView cityView = view.findViewById(R.id.cityText);
        TextView parkView = view.findViewById(R.id.parkText);

        Button backButton = view.findViewById(R.id.back_park);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.from_simulator_to_selection);
        });

       if (getArguments() == null){
           Toast.makeText(getContext(), "Error passing arguments..." , Toast.LENGTH_SHORT).show();

       }else{
           park = null;
           reserveSuccess = false;
           String city = getArguments().getString("city") + ",";
           String parkText = getArguments().getString("park") + ".";

           cityView.setText(city);
           parkView.setText(parkText);

           city = getArguments().getString("city");
           parkText = getArguments().getString("park");

           Client.forceWait = true;
           Client.getClient().sendMessageToServer(new Message(RequestType.GET_PARK_SLOTS, new Park(city, parkText)));
           while(Client.forceWait){
               System.out.print("");
           }

           if (park == null){
               Toast.makeText(getContext(), "Error loading park slots" , Toast.LENGTH_SHORT).show();

               NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
               navController.navigate(R.id.from_simulator_to_selection);
               return null;
           }

           initVariables(view);

           handler.post(carArrivalRunner);
           handler.post(carExitRunner);
       }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove all callbacks when the Fragment is destroyed to avoid memory leaks
        handler.removeCallbacks(carArrivalRunner);
        handler.removeCallbacks(carExitRunner);
    }

    private final Runnable carArrivalRunner = new Runnable() {
        @Override
        public void run() {
            if (canAcceptNewCar)
                handleCarArrival();
            int delay = random.nextInt(3000) + 9000; // Random delay between 7-10 seconds (3000-7000ms)
            handler.postDelayed(this, delay);
        }
    };

    private final Runnable carExitRunner = new Runnable() {
        @Override
        public void run() {
            if (!takenSpots.isEmpty()){
                int carToLeave = random.nextInt(takenSpots.size());
                ParkingSlot ps = takenSpots.get(carToLeave);

                handleCarExit(ps);
            }
            int delay = random.nextInt(3000) + 18000; // Random delay between 3-7 seconds (3000-7000ms)
            handler.postDelayed(this, delay);
        }
    };
    private void initVariables(View view){
        if (park == null || view == null)
            return;

        arr_right2 = view.findViewById(R.id.arrow_right_2);
        arr_right2.setVisibility(View.INVISIBLE);
        arr_right3 = view.findViewById(R.id.arrow_right_3);
        arr_right3.setVisibility(View.INVISIBLE);
        parentLayout = view.findViewById(R.id.mainFrame);
        entryBlock = view.findViewById(R.id.enter_block);
        exitBlock = view.findViewById(R.id.exit_block);
        entryStatus = view.findViewById(R.id.entryStatus);
        exitStatus = view.findViewById(R.id.exitStatus);

        slots = new ImageView[][]{
                {view.findViewById(R.id.park00), view.findViewById(R.id.park01), view.findViewById(R.id.park02), view.findViewById(R.id.park03), view.findViewById(R.id.park04), view.findViewById(R.id.park05), view.findViewById(R.id.park06)},
                {view.findViewById(R.id.park10), view.findViewById(R.id.park11), view.findViewById(R.id.park12), view.findViewById(R.id.park13), view.findViewById(R.id.park14), view.findViewById(R.id.park15), view.findViewById(R.id.park16)},
                {view.findViewById(R.id.park20), view.findViewById(R.id.park21), view.findViewById(R.id.park22), view.findViewById(R.id.park23), view.findViewById(R.id.park24), view.findViewById(R.id.park25), view.findViewById(R.id.park26)},
                {view.findViewById(R.id.park30), view.findViewById(R.id.park31), view.findViewById(R.id.park32), view.findViewById(R.id.park33), view.findViewById(R.id.park34), view.findViewById(R.id.park35), view.findViewById(R.id.park36)}
        };

        // resetting slots
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 7; j++){
                slots[i][j].setVisibility(TextView.INVISIBLE);
            }

        for (int i = 0; i < park.getSlots().length; i++)
            for (int j = 0; j < park.getSlots()[0].length; j++){
                slots[i][j].setVisibility(TextView.VISIBLE);
                avblSpots.add(new ParkingSlot(slots[i][j], i, j));
            }

    }

    private void handleCarExit(ParkingSlot slot){
        if (slot == null)
            return;
        new Thread(()->{
            takenSpots.remove(slot);
            avblSpots.add(slot);
            requireActivity().runOnUiThread(()->{
                slot.getImg().setImageResource(0);
                slot.getImg().setBackgroundResource(R.drawable.parking_spot_border);

                ImageView exitingCar = new ImageView(requireContext());
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

    private void handleCarArrival(){
        canAcceptNewCar = false;
        new Thread(()->{
            ImageView car = new ImageView(requireContext());
            requireActivity().runOnUiThread(()->{
                car.setX(ParkData.CAR_START_X);
                car.setY(ParkData.CAR_START_Y);
                car.setImageResource(R.drawable.car);
                car.setVisibility(View.VISIBLE);

                parentLayout.addView(car);

                entryBlock.setImageResource(R.drawable.car_block_closed);

            });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            requireActivity().runOnUiThread(()->{
                entryBlock.setImageResource(R.drawable.car_block_yellow);
            });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!avblSpots.isEmpty()){
                    int roll = random.nextInt(avblSpots.size());
                    ParkingSlot ps = avblSpots.get(roll);
                    requireActivity().runOnUiThread(()->{
                        ps.getImg().setBackgroundResource(R.drawable.parking_spot_border_yellow);
                    });
                    avblSpots.remove(ps);

                    moveCar(ps, car);
                }else{
                    requireActivity().runOnUiThread(()->{
                        entryBlock.setImageResource(R.drawable.car_block_closed);
                        parentLayout.removeView(car);
                    });
                }



        }).start();
    }
    private void carMoveToExit(ParkingSlot slot, ImageView exitingCar, ViewGroup parent){
        if (exitingCar == null)
            return;
        requireActivity().runOnUiThread(()->{
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(exitingCar, "y", exitingCar.getY(), exitBlock.getY() + 30);
            animatorY.setDuration((slot.getRow() + 1) * 1000L);
            animatorY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Change to yellow block
                    exitBlock.setImageResource(R.drawable.car_block_yellow);

                    // Delay using postDelayed instead of Thread.sleep
                    exitBlock.postDelayed(() -> {
                        exitBlock.setImageResource(R.drawable.car_block_open);

                        // Delay to close the block and remove the car
                        exitBlock.postDelayed(() -> {
                            exitBlock.setImageResource(R.drawable.car_block_closed);
                            parent.removeView(exitingCar);
                            slot.setTimeOfEntry(-1);
                            }, 1500);
                        }, 2500);
                }
            });
            animatorY.start();
        });
    }

    private void moveCar(ParkingSlot toSlot, ImageView car){
        if (toSlot == null)
            return;
        requireActivity().runOnUiThread(()->{
            entryBlock.setImageResource(R.drawable.car_block_open);
        });
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
        if (car == null || slot == null)
            return;

        requireActivity().runOnUiThread(() -> {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(car, "x", car.getX(), entryBlock.getX() - 50);
            animatorX.setDuration(2000);

            animatorX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        Thread.sleep(1500);
                        car.setImageResource(R.drawable.car_going_down);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    entryBlock.setImageResource(R.drawable.car_block_closed);
                    canAcceptNewCar = true;
                    moveOnY(slot, car);

                }
            });
            animatorX.start();
        });
    }

    private void moveOnY(ParkingSlot slot, ImageView car){
        if (car == null || slot == null)
            return;
        float yDest;
        if (slot.getRow() == 2)
            yDest = arr_right2.getY();
        else
            yDest = arr_right3.getY();
        ImageView arrow = new ImageView(requireContext());
        requireActivity().runOnUiThread(() -> {
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(car, "y", car.getY(), yDest);
            animatorY.setDuration((slot.getCol() + 1) * 1500L);

            animatorY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        Thread.sleep(1500);
                        car.setImageResource(R.drawable.car);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    moveOnX(slot, car);
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

    private void moveOnX(ParkingSlot slot, ImageView car){
        if (car == null || slot == null)
            return;
            // Run UI-related code on the main thread
        ImageView arrow = new ImageView(requireContext());
        requireActivity().runOnUiThread(() -> {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(car, "x", car.getX(), slot.getImg().getX());
            animatorX.setDuration((slot.getCol() + 1) * 1000L);
            animatorX.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation){
                    if (slot.getRow() <= 1)
                        handler.postDelayed(()->{
                            entryBlock.setImageResource(R.drawable.car_block_closed);
                            canAcceptNewCar = true;
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

}
