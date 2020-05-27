//package com.company;

import java.io.FileNotFoundException;
import java.util.*;

public class my_player {

    public static void main(String[] args) throws FileNotFoundException {
        GO go = new GO();
        // List<List<Integer>> valid_moves = go.get_valid_moves(go.my_piece, go.current_board, go.previous_board);

       /* System.out.println("--------------");
        for(List<Integer> move : valid_moves){
            System.out.println("" + move.get(0) + "," + move.get(1));
        }
        System.out.println("--------------");*/

       /*Random random = new Random();
        go.write_output(valid_moves.get(random.nextInt(valid_moves.size())));*/

       /*for(List<Integer> list : go.horizontal_counterpart.keySet()){
           List<Integer> val = go.horizontal_counterpart.get(list);
           System.out.println("" + list.get(0) + '-' + list.get(1) + "---" + val.get(0) + '-' + val.get(1));
       }*/

      /* boolean flag_hs;
       boolean flag_vs;
       boolean flag_ds;
       boolean flag_ads;

       flag_hs = go.is_horizontally_symmetric(go.current_board);
       flag_vs = go.is_vertically_symmetric(go.current_board);
       flag_ds = go.is_diagonally_symmetric(go.current_board);
       flag_ads = go.is_anti_diagonally_symmetric(go.current_board);*/

      /* System.out.println("--------------");
       System.out.println(flag_hs);
       System.out.println(flag_vs);
       System.out.println(flag_ds);
       System.out.println(flag_ads);
       System.out.println("--------------");*/

      /* List<List<Integer>> filtered_valid_moves = new ArrayList<>(valid_moves);
       if(flag_hs){
           filtered_valid_moves = go.filter_horizontal_symmetry(filtered_valid_moves);
       }
       if(flag_vs){
           filtered_valid_moves = go.filter_vertical_symmetry(filtered_valid_moves);
       }
       if(flag_ds){
           filtered_valid_moves = go.filter_diagonal_symmetry(filtered_valid_moves);
       }
       if(flag_ads){
           filtered_valid_moves = go.filter_anti_diagonal_symmetry(filtered_valid_moves);
       }
       /*System.out.println("--------------");
       for(List<Integer> move : filtered_valid_moves){
           System.out.println("" + move.get(0) + "," + move.get(1));
       }
       System.out.println("--------------");*/

        if(go.is_first_move){
            Integer[] arr = {2, 2};
            List<Integer> list = Arrays.asList(arr);
            if(go.my_piece == GO.BLACK){
                go.write_output(list);
            } else {
                if(go.filtered_valid_moves.contains(list)){
                    go.write_output(list);
                } else {
                    Integer[] arr2 = {2, 1};
                    List<Integer> list2 = Arrays.asList(arr2);
                    go.write_output(list2);
                }
            }
            go.set_moves_left();
            return;
        }

        if(go.did_opponent_pass && go.my_PAS > go.opp_PAS){
            Integer[] arr = {-1, -1};
            List<Integer> list = Arrays.asList(arr);
            go.write_output(list);
            return;
        }

       /*System.out.println("--------------");
       System.out.println(go.my_PAS);
       System.out.println(go.my_FAS);
       System.out.println(go.opp_PAS);
       System.out.println(go.opp_FAS);
       System.out.println("--------------");*/

        // Random random = new Random();
        //go.write_output(go.filtered_valid_moves.get(random.nextInt(go.filtered_valid_moves.size())));

       /* List<Integer> test_move = filtered_valid_moves.get(random.nextInt(filtered_valid_moves.size()));
        //Integer[] test_move_arr = new Integer[] {2, 0};
        //List<Integer> test_move = Arrays.asList(test_move_arr);
        System.out.println("" + test_move.get(0) + " " + test_move.get(1));
        GO go2 = new GO(go, test_move);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print("" + go2.current_board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(go2.my_piece);
        System.out.println(go2.my_PAS);
        System.out.println(go2.opp_PAS);
        System.out.println(go2.my_FAS);
        System.out.println(go2.opp_FAS);*/

        TreeNode root = new TreeNode(null, go, true, 0, null);
        int terminal = go.moves_left >= 4 ? 4 : 2;
        Tree tree = new Tree(root, go.my_piece, terminal);
        tree.prune(root, -10000.0, 10000.0, true);
        //System.out.println("F-" + d);
        go.set_moves_left();
        //System.out.println(go.moves_left);
        go.write_output(tree.find_best_move());
    }

}


