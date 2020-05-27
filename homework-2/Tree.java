//package com.company;

import java.util.*;

public class Tree {
    TreeNode root;
    int rootPlayer;
    int terminal;

    public Tree(TreeNode root, int rootPlayer, int terminal) {
        this.root = root;
        this.rootPlayer = rootPlayer;
        this.terminal = terminal;
    }

    public double prune(TreeNode node, double alpha, double beta, boolean isMax) {
        if (node.depth == this.terminal) {
            return node.go.my_FAS - node.go.opp_FAS;
        }
        if (isMax) {
            if (!node.children_generated) {
                node.generate_children();
            }
            //System.out.println("" + node.children.size() + "---" + node.depth);
            for (TreeNode child : node.children) {
                //System.out.println("" + child.parent_move.get(0) + "-" + child.parent_move.get(1));
                node.v = Math.max(node.v, prune(child, alpha, beta,false));
                alpha = Math.max(alpha, node.v);
                if (alpha >= beta) {
                    break;
                }
            }
            return node.v;
        } else {
            if (!node.children_generated) {
                node.generate_children();
            }
            for (TreeNode child : node.children) {
                //System.out.println("" + child.parent_move.get(0) + "-" + child.parent_move.get(1));
                node.v = Math.min(node.v, prune(child, alpha, beta, true));
                beta = Math.max(alpha, node.v);
                if (alpha >= beta) {
                    break;
                }
            }
            return node.v;
        }
    }

    public List<Integer> find_best_move(){
        double max_val = -10000.0;
        List<Integer> best_move = new ArrayList<>();
        for(TreeNode child : this.root.children){
            if(child.v > max_val){
                max_val = child.v;
                best_move = child.parent_move;
            }
        }
        return best_move;
    }
}


