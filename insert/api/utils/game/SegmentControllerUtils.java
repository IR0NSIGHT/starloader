package api.utils.game;

import api.ModPlayground;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorTree;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.core.GlUtil;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.vecmath.Vector3f;
import java.util.ArrayList;

public class SegmentControllerUtils {
    /**
     * Gets a Vector3f of the entity's current direction.
     */
    public static Vector3f getDirection(SegmentController controller) {
        return GlUtil.getForwardVector(new Vector3f(), controller.getWorldTransform());
    }
    @Nullable
    public static ReactorElement getChamberFromElement(@NotNull ManagedUsableSegmentController<?> controller, @NotNull ElementInformation blockId){
        for (ReactorElement child : getAllChambers(controller)) {
            if(child.type == blockId.id) return child;
        }
        return null;
    }
    @NotNull
    public static ArrayList<ReactorElement> getAllChambers(ManagedUsableSegmentController<?> controller){
        ReactorTree mainReactor = controller.getManagerContainer().getPowerInterface().getActiveReactor();
        ArrayList<ReactorElement> r = new ArrayList<ReactorElement>(mainReactor.children.size());
        for (ReactorElement child : mainReactor.children) {
            getChildrenChambers(child, r);
        }
        return r;
    }
    @NotNull
    private static void getChildrenChambers(ReactorElement element, ArrayList<ReactorElement> arr) {
        //Add children to array
        if (!element.children.isEmpty()) {
            for (ReactorElement child : element.children) {
                getChildrenChambers(child, arr);
            }
        }
        //Add self
        arr.add(element);
    }
}
