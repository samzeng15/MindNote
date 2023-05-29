package graphvisualiser;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.javatuples.Pair;


import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.*;
import static guru.nidi.graphviz.engine.Graphviz.*;
// using graphviz to visualise graph
public class GraphVisualiser {
    public static void GenerateGraph(ArrayList<Pair<Integer, String>> input) throws IOException {
        if (input == null) {
            return;
        }
        //System.out.println("generating graph");
        MutableGraph g = mutGraph("example1").setDirected(false).use((gr, ctx) -> {
            Stack<Pair<Integer, String>> stk = new Stack<>();
            stk.add(input.get(0));
            mutNode(input.get(0).getValue1());
            for (Pair<Integer, String> p : input.subList(1, input.size())) {
                int level = p.getValue0();
                String text = p.getValue1();
                if (level <= stk.peek().getValue0()) {
                    //System.out.println("level " + level + " less than or equal to in stack");
                    while (!stk.empty()) {
                        //pop until the level represents child of stack level
                        if (level <= stk.peek().getValue0()) {
                            stk.pop();
                        }
                        else {
                            break;
                        }
                    }
                }
                if (stk.empty()) {
                    //System.out.println("stack was empty");
                    mutNode(text);
                    stk.add(p);
                    continue;
                }
                //System.out.println("=============");
                //System.out.println(stk.peek().getValue1());
                //System.out.println(text);
                mutNode(text).addLink(mutNode(stk.peek().getValue1()));
                stk.add(p);
            }
        });
        Graphviz.fromGraph(g).width(2000).render(Format.PNG).toFile(new File("Graphs/OutputGraph.png"));
    }
}
