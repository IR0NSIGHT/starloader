package api.utils.game;

import api.ModPlayground;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.*;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorTree;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.GlUtil;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.vecmath.Vector3f;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;

public class SegmentControllerUtils {
    /**
     * Gets a Vector3f of the entity's current direction.
     */
    @NotNull
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

    @NotNull
    public static ObjectArrayList<ManagerModule<?, ?, ?>> getManagerModules(ManagedUsableSegmentController<?> ent){
        return ent.getManagerContainer().getModules();
    }

    @Nullable
    public static <EM extends UsableElementManager<?, ?, ?>> EM getElementManager(ManagedUsableSegmentController<?> ent, Class<EM> classType){
        for (ManagerModule<?, ?, ?> managerModule : getManagerModules(ent)) {
            UsableElementManager<?, ?, ?> elementManager = managerModule.getElementManager();
            if(elementManager.getClass().equals(classType)){
                return (EM) elementManager;
            }
        }
        return null;
    }

    /**
     * Gets an arraylist of players currently attached to the entity.
     */
    public static ArrayList<PlayerState> getAttachedPlayers(SegmentController controller) {
        if (controller instanceof PlayerControllable) {
            return new ArrayList<PlayerState>(((PlayerControllable) controller).getAttachedPlayers());
        } else {
            return new ArrayList<PlayerState>();
        }
    }

    public <CM extends ElementCollectionManager> ArrayList<ElementCollectionManager> getCollectionManagers(ManagedUsableSegmentController<?> ent, Class<CM> classType){
        ArrayList<ElementCollectionManager> ecms = new ArrayList<ElementCollectionManager>();
        for (ManagerModule<?, ?, ?> module : ent.getManagerContainer().getModules()) {
            if(module instanceof ManagerModuleCollection){
                for (Object cm : ((ManagerModuleCollection) module).getCollectionManagers()) {
                    if(cm.getClass().equals(classType)) {
                        ecms.add((ElementCollectionManager) cm);
                    }
                }
            }else if(module instanceof ManagerModuleSingle){
                ElementCollectionManager cm = ((ManagerModuleSingle) module).getCollectionManager();
                if(cm.getClass().equals(classType)){
                    ecms.add(cm);
                }
            }//else{ something broke }
        }
        return ecms;
    }
    public ArrayList<ElementCollectionManager> getAllCollectionManagers(ManagedUsableSegmentController<?> ent){
        ArrayList<ElementCollectionManager> ecms = new ArrayList<ElementCollectionManager>();
        for (ManagerModule<?, ?, ?> module : ent.getManagerContainer().getModules()) {
            if(module instanceof ManagerModuleCollection){
                for (Object cm : ((ManagerModuleCollection) module).getCollectionManagers()) {
                    ecms.add((ElementCollectionManager) cm);
                }
            }else if(module instanceof ManagerModuleSingle){
                ElementCollectionManager cm = ((ManagerModuleSingle) module).getCollectionManager();
                ecms.add(cm);
            }
        }
        return ecms;
    }

    /**
     * Gets an addon based on its PlayerUsableId, which is faster than the iterative method
     */
    @Nullable
    public static PlayerUsableInterface getAddon(ManagedUsableSegmentController<?> ent, long playerUsableId){
        return ent.getManagerContainer().getPlayerUsable(playerUsableId);
    }

    /**
     * Get a player usable (addon) based on its class type. will be slow.
     */
    @Nullable
    public static <P extends PlayerUsableInterface> P getAddon(ManagedUsableSegmentController<?> ent, Class<P> classType){
        ObjectCollection<PlayerUsableInterface> usable = ent.getManagerContainer().getPlayerUsable();
        for (PlayerUsableInterface i : usable) {
            if(i.getClass().equals(classType)){
                return (P) i;
            }
        }
        return null;
    }
}
