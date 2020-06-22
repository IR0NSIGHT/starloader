//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view;

import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.UnicodeFont;
import org.schema.common.util.ByteUtil;
import org.schema.common.util.linAlg.Matrix4fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.manager.ingame.BuildToolsManager;
import org.schema.game.client.controller.manager.ingame.PlayerGameControlManager;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.beam.BeamDrawer;
import org.schema.game.client.view.cubes.CubeData;
import org.schema.game.client.view.cubes.CubeDataPool;
import org.schema.game.client.view.cubes.CubeMeshBufferContainer;
import org.schema.game.client.view.cubes.cubedyn.CubeMeshManagerBulkOptimized.CubeMeshDynOpt;
import org.schema.game.client.view.cubes.lodshapes.LodDraw;
import org.schema.game.client.view.cubes.occlusion.Occlusion;
import org.schema.game.client.view.cubes.shapes.BlockShapeAlgorithm;
import org.schema.game.client.view.cubes.shapes.BlockStyle;
import org.schema.game.client.view.cubes.shapes.orientcube.Oriencube;
import org.schema.game.client.view.effects.Shadow;
import org.schema.game.client.view.effects.ShieldDrawer;
import org.schema.game.client.view.effects.segmentcontrollereffects.RunningEffect;
import org.schema.game.client.view.effects.segmentcontrollereffects.SegmentControllerEffectDrawer;
import org.schema.game.client.view.shader.CubeMeshQuadsShader13;
import org.schema.game.client.view.shader.CubeMeshQuadsShader13.CubeTexQuality;
import org.schema.game.client.view.textbox.AbstractTextBox;
import org.schema.game.client.view.textbox.Replacement;
import org.schema.game.client.view.tools.SingleBlockDrawer;
import org.schema.game.common.controller.SegmentBuffer;
import org.schema.game.common.controller.SegmentBufferInterface;
import org.schema.game.common.controller.SegmentBufferIteratorEmptyInterface;
import org.schema.game.common.controller.SegmentBufferIteratorInterface;
import org.schema.game.common.controller.SegmentBufferManager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.world.DrawableRemoteSegment;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;
import org.schema.game.common.data.world.SegmentDataIntArray;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.space.PlanetCore;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.AbstractScene;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.FrameBufferObjects;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.DebugBox;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.TransformableSubSprite;
import org.schema.schine.graphicsengine.forms.debug.DebugDrawer;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import org.schema.schine.graphicsengine.shader.Shaderable;
import org.schema.schine.graphicsengine.shader.ShaderLibrary.CubeShaderType;
import org.schema.schine.input.Keyboard;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.container.TransformTimed;

public class SegmentDrawer implements Drawable {
    public static final SegmentOcclusion segmentOcclusion = new SegmentOcclusion();
    public static final int DISTANCE_VIEWER = 0;
    public static final int DISTANCE_CAMERA = 1;
    public static final int LIGHTING_THREAD_COUNT = 2;
    private static final int ADDITIVE = 1;
    private static final int MULTIPLICATIVE = 2;
    public static final float ORDERED_DRAW_DIST_OPAUQ_THRESHOLD = 100.0F;
    public static int distanceMode = 1;
    public static boolean forceFullLightingUpdate;
    public static CubeDataPool dataPool;
    public static boolean directModelview = false;
    public static boolean seperateDrawing = true;
    public static boolean reinitializeMeshes;
    public static CubeMeshQuadsShader13 shader;
    static int threadCount = 0;
    static int meshCounter = 0;
    private SegmentLodDrawer lod = new SegmentLodDrawer(this);
    private static Transform beamTmp = new Transform();
    private static int blendFunc = 0;
    protected final ObjectArrayList<DrawableRemoteSegment> generatedSegments = new ObjectArrayList(512);
    private final ObjectOpenHashSet<DrawableRemoteSegment> updateLocks = new ObjectOpenHashSet();
    private final ObjectArrayList<SegmentController> segmentControllers;
    private final GameClientState state;
    private final SegmentDrawer.SegmentLightingUpdateThreadManager segmentLightingUpdate;
    private final SegmentDrawer.SegmentSorterThread segmentSorter;
    private final List<DrawableRemoteSegment> removedSegments = new ObjectArrayList();
    private final List<DrawableRemoteSegment> disposedSegs = new ObjectArrayList();
    private final Vector3i start = new Vector3i();
    private final Vector3i end = new Vector3i();
    private final ObjectOpenHashSet<DrawableRemoteSegment> afterGenerated = new ObjectOpenHashSet();
    public int sortingSerial = 1;
    public int inDrawBufferCount;
    public SegmentDrawer.SegDrawStats stats = new SegmentDrawer.SegDrawStats();
    public SegmentDrawer.TextBoxSeg textBoxes = new SegmentDrawer.TextBoxSeg();
    protected DrawableRemoteSegment[] drawnSegments;
    protected DrawableRemoteSegment[] drawnSegmentsBySegmentController;
    protected DrawableRemoteSegment[] drawnSegmentsBySegmentControllerDouble;
    protected DrawableRemoteSegment[] drawnBlendedSegments;
    protected DrawableRemoteSegment[] drawnOpaqueSegments;
    protected DrawableRemoteSegment[] drawnBlendedSegmentsBySegment;
    protected DrawableRemoteSegment[] drawnSegmentsDouble;
    protected DrawableRemoteSegment[] deactivatedSegments;
    protected DrawableRemoteSegment[] deactivatedSegmentsDouble;
    Vector3f segPosTmp = new Vector3f();
    boolean ff = false;
    AbstractTextBox textBox;
    int blendedPointer = 0;
    int blendedPointerBySeg = 0;
    int opaquePointer = 0;
    Vector3f beforeShift = new Vector3f(-1000000.0F, -1000000.0F, 1000000.0F);
    Vector3f afterShift = new Vector3f();
    int bb = 0;
    short updateNum;
    private boolean requireFullDrawFromSort;
    private Int2ObjectOpenHashMap<ObjectArrayList<SegmentDrawer.SAABB>> saabbMapLive = new Int2ObjectOpenHashMap();
    private SegmentDrawer.SegmentRenderPass segmentRenderPass = null;
    private int drawnSegmentsPointer;
    private boolean firstDraw = true;
    private boolean recreate = true;
    private HashSet<DrawableRemoteSegment> disposable = new HashSet();
    private SegmentController currentSegmentController;
    private long timeDrawn;
    private boolean resorted;
    private Vector3f minBBOut = new Vector3f();
    private Vector3f maxBBOut = new Vector3f();
    private Vector3f minSBBBOut = new Vector3f();
    private Vector3f maxSBBBOut = new Vector3f();
    private Vector3f minSBBBOutC = new Vector3f();
    private Vector3f maxSBBBOutC = new Vector3f();
    public static final long WARNING_MARGIN = 50L;
    private Vector3f posOut = new Vector3f();
    private boolean sorterUpdate;
    private Matrix4f modelview = new Matrix4f();
    private FloatBuffer modelviewBuffer = BufferUtils.createFloatBuffer(16);
    private Vector3i lastTransform = new Vector3i(-1, 0, 0);
    private boolean frustumCulling;
    private Matrix4f outMatrix;
    private Matrix4f mMatrix;
    private boolean culling;
    private boolean cullFace;
    private boolean turnOffAllLight;
    private int max;
    private boolean flagMaxChanged;
    private SegAABBDrawer d;
    private ObjectArrayList<SegmentOcclusion> occlusionsUsed;
    private RunningEffect effect;
    private SegmentController currentDrawing;
    private long blinkingTime;
    private final ObjectArrayList<SegmentData> lodShapes;
    private final ElementCollectionDrawer elementCollectionDrawer;
    private LodDraw[] lodDraws;
    private int lodPointer;
    private Transform tmpTrns;
    private Quat4f quatTmp;
    private Vector3f vecTmp;
    private Matrix3f matTmp;
    private SegmentController filter;
    private int filterDrawn;
    private UnicodeFont textBoxFont;
    private Matrix3f rotTmp;
    private static final float BLENDED_NEAR_THRESH = 30.0F;
    private static final float BLENDED_NEAR_THRESH_SQUARED = 900.0F;
    private static final float ORDERED_DRAW_DIST_OPAUQ_THRESHOLD_SQUARED = 10000.0F;
    public static float LOD_THRESH_SQUARED;
    private List<PlanetCore> currentCores;
    private boolean finished;

    public SegmentDrawer(GameClientState var1) {
        this.frustumCulling = EngineSettings.G_FRUSTUM_CULLING.isOn();
        this.outMatrix = new Matrix4f();
        this.mMatrix = new Matrix4f();
        this.culling = true;
        this.cullFace = true;
        this.max = (Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState();
        this.d = new SegAABBDrawer();
        this.occlusionsUsed = new ObjectArrayList();
        this.currentDrawing = null;
        this.lodShapes = new ObjectArrayList();
        this.lodDraws = new LodDraw[4096];

        for(int var2 = 0; var2 < this.lodDraws.length; ++var2) {
            this.lodDraws[var2] = new LodDraw();
        }

        this.lodPointer = 0;
        this.tmpTrns = new Transform();
        this.quatTmp = new Quat4f();
        this.vecTmp = new Vector3f();
        this.matTmp = new Matrix3f();
        this.rotTmp = new Matrix3f();
        this.currentCores = new ObjectArrayList();
        this.state = var1;
        shader = new CubeMeshQuadsShader13();
        dataPool = new CubeDataPool();
        this.drawnSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.drawnBlendedSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.drawnOpaqueSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.drawnBlendedSegmentsBySegment = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.drawnSegmentsDouble = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.deactivatedSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.deactivatedSegmentsDouble = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.drawnSegmentsBySegmentController = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.drawnSegmentsBySegmentControllerDouble = new DrawableRemoteSegment[dataPool.POOL_SIZE];
        this.elementCollectionDrawer = new ElementCollectionDrawer(var1);
        this.segmentLightingUpdate = new SegmentDrawer.SegmentLightingUpdateThreadManager(2);
        this.segmentSorter = new SegmentDrawer.SegmentSorterThread();
        this.segmentControllers = new ObjectArrayList(128);
    }

    private void afterResort() {
        synchronized(this.generatedSegments) {
            synchronized(this.disposable) {
                for(int var3 = 0; var3 < this.drawnSegmentsPointer; ++var3) {
                    if (this.deactivatedSegments[var3] != null && this.deactivatedSegments[var3].getSortingSerial() < this.sortingSerial) {
                        this.deactivatedSegments[var3].setActive(false);
                        this.disposable.add(this.deactivatedSegments[var3]);
                    }

                    this.afterGenerated.remove(this.drawnSegments[var3]);
                }

                this.disposable.addAll(this.afterGenerated);
                this.afterGenerated.clear();
                synchronized(this.getRemovedSegments()) {
                    this.disposable.addAll(this.getRemovedSegments());
                    this.getRemovedSegments().clear();
                }

                Iterator var9 = this.disposable.iterator();

                while(var9.hasNext()) {
                    DrawableRemoteSegment var4;
                    if (!(var4 = (DrawableRemoteSegment)var9.next()).isInUpdate()) {
                        this.state.getWorldDrawer().getFlareDrawerManager().clearSegment(var4);
                        var4.releaseContainerFromPool();
                        var4.disposeAll();
                        var4.setInUpdate(false);
                        var4.setActive(false);
                        this.disposedSegs.add(var4);
                    }
                }

                this.disposedSegs.isEmpty();
                if (this.disposedSegs.size() != this.disposable.size()) {
                    System.err.println("[SEGDRAWER] not Disposed LEFT: " + this.disposedSegs.size() + "/" + this.disposable.size());
                }

                this.disposable.removeAll(this.disposedSegs);
                this.disposedSegs.clear();
            }
        }

        synchronized(this.segmentSorter.waitForApply) {
            this.segmentSorter.applied = true;
            this.segmentSorter.waitForApply.notify();
        }

        this.sorterUpdate = true;
        dataPool.cleanUp(this.sortingSerial);
    }

    private boolean checkNeedsMeshUpdate(DrawableRemoteSegment var1, long var2) {
        if (var1.occlusionFailed && !var1.needsMeshUpdate() && !var1.isInUpdate()) {
            SegmentBufferManager var4 = (SegmentBufferManager)var1.getSegmentController().getSegmentBuffer();
            this.start.set(var1.pos);
            this.end.set(var1.pos);
            this.start.sub(64, 64, 64);
            this.end.add(64, 96, 64);
            long var5;
            if ((var5 = var2 - var1.occlusionFailTime) > 10000L || var5 > 100L && var4.getLatestChangedInArea(this.start, this.end, false) > var1.lastLightingUpdateTime) {
                var1.occlusionFailed = false;
                var1.setNeedsMeshUpdate(true);
            }
        }

        if ((var1.needsMeshUpdate() || var1.getCurrentCubeMesh() == null) && !var1.isInUpdate()) {
            this.segmentLightingUpdate.addToUpdateQueue(var1);
            if (var1.getCurrentCubeMesh() == null) {
                return false;
            }
        }

        return true;
    }

    public int getQueueSize() {
        return this.segmentLightingUpdate.getQueueSize();
    }

    public void cleanUp() {
        this.cleanUpCubeMeshes();
    }

    public void draw() {
        assert this.segmentRenderPass != null;

        this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getBuildToolsManager();
        if (this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSegmentControlManager().getSegmentBuildController().isTreeActive() || this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getSegmentBuildController().isTreeActive()) {
            this.draw(shader, ShaderLibrary.getCubeShader(this.getState().getWorldDrawer().isSpotLightSupport() | CubeShaderType.LIGHT_ALL.bit), EngineSettings.G_DRAW_SHIELDS.isOn(), true, segmentOcclusion, this.state.getNumberOfUpdate());
        } else {
            this.draw(shader, ShaderLibrary.getCubeShader(this.getState().getWorldDrawer().isSpotLightSupport()), EngineSettings.G_DRAW_SHIELDS.isOn(), true, segmentOcclusion, this.state.getNumberOfUpdate());
        }

        this.drawCubeLod(false);
    }

    public void drawElementCollectionsFromFrameBuffer(FrameBufferObjects var1, float var2) {
        this.getElementCollectionDrawer().drawFrameBuffer(var1, var2);
    }

    public void drawElementCollectionsToFrameBuffer(FrameBufferObjects var1) {
        this.getElementCollectionDrawer().drawToFrameBuffer(var1);
    }

    public boolean drawCheckElementCollections() {
        return this.getElementCollectionDrawer().checkDraw();
    }

    public void drawCubeLod(boolean var1) {
        boolean var2 = Keyboard.isKeyDown(60);
        this.lodPointer = 0;
        int var9;
        if (!this.lodShapes.isEmpty()) {
            int var3 = this.lodShapes.size();

            for(int var4 = 0; var4 < var3; ++var4) {
                SegmentData var5;
                if ((var5 = (SegmentData)this.lodShapes.get(var4)).getLodTypeAndOrientcubeIndex() != null) {
                    Vector3i var6 = var5.getSegment().pos;
                    int var7 = var5.drawingLodShapes.size();

                    for(int var8 = 0; var8 < var7; ++var8) {
                        var9 = var5.drawingLodShapes.get(var8);
                        short var10;
                        if (ElementKeyMap.isValidType(var10 = var5.getLodTypeAndOrientcubeIndex()[var8 << 1]) && ElementKeyMap.getInfoFast(var10).hasLod()) {
                            ElementInformation var24 = ElementKeyMap.getInfoFast(var10);
                            boolean var26 = var5.isActive(var9);
                            int var13 = var24 != null ? var24.getModelCount(var26) : 1;

                            for(int var14 = 0; var14 < var13; ++var14) {
                                while(this.lodPointer >= this.lodDraws.length) {
                                    int var15 = this.lodDraws.length;
                                    this.lodDraws = (LodDraw[])Arrays.copyOf(this.lodDraws, this.lodDraws.length << 1);

                                    for(int var16 = var15; var16 < this.lodDraws.length; ++var16) {
                                        this.lodDraws[var16] = new LodDraw();
                                    }
                                }

                                LodDraw var27;
                                (var27 = this.lodDraws[this.lodPointer]).lightingAndPos = var5.getLodData();
                                var27.pointer = var8 * SegmentData.lodDataSize;
                                var27.type = var10;
                                var27.faulty = false;
                                var27.mesh = var24.getModel(var14, var26);

                                assert var27.mesh != null;

                                short var28 = var5.getLodTypeAndOrientcubeIndex()[(var8 << 1) + 1];
                                Oriencube var17 = (Oriencube)BlockShapeAlgorithm.algorithms[5][var24.blockStyle == BlockStyle.SPRITE ? var28 % 6 << 2 : var28];
                                if (var24.getId() == 104) {
                                    var17 = Oriencube.getOrientcube(var28 % 6, var28 % 6 > 1 ? 0 : 2);
                                }

                                var27.transform.set(var5.getSegmentController().getWorldTransformOnClient());
                                SegmentData.getPositionFromIndexWithShift(var9, var6, this.vecTmp);
                                this.tmpTrns.set(var17.getBasicTransform());
                                this.tmpTrns.origin.set(0.0F, 0.0F, 0.0F);
                                this.quatTmp.set(var27.mesh.getInitialQuadRot());
                                this.matTmp.set(this.quatTmp);
                                if (var24.getBlockStyle() == BlockStyle.SPRITE) {
                                    this.rotTmp.setIdentity();
                                    this.rotTmp.rotX((float)SingleBlockDrawer.timesR * 1.5707964F);
                                    this.tmpTrns.basis.mul(this.rotTmp);
                                }

                                this.tmpTrns.basis.mul(this.matTmp);
                                this.tmpTrns.origin.set(var27.mesh.getInitionPos());
                                this.tmpTrns.origin.add(this.vecTmp);
                                Matrix4fTools.transformMul(var27.transform, this.tmpTrns);
                                ++this.lodPointer;
                            }
                        } else {
                            while(this.lodPointer >= this.lodDraws.length) {
                                int var11 = this.lodDraws.length;
                                this.lodDraws = (LodDraw[])Arrays.copyOf(this.lodDraws, this.lodDraws.length << 1);

                                for(int var12 = var11; var12 < this.lodDraws.length; ++var12) {
                                    this.lodDraws[var12] = new LodDraw();
                                }
                            }

                            LodDraw var23;
                            (var23 = this.lodDraws[this.lodPointer]).lightingAndPos = var5.getLodData();
                            var23.pointer = var8 * SegmentData.lodDataSize;
                            var23.type = var10;
                            var23.faulty = true;
                            ++this.lodPointer;
                        }
                    }
                }
            }
        }

        if (this.lodPointer > 0) {
            if (var2) {
                GlUtil.printGlErrorCritical();
            }

            Arrays.sort(this.lodDraws, 0, this.lodPointer);
            this.lodShapes.size();
            Shader var18 = null;
            if (var2) {
                GlUtil.printGlErrorCritical();
            }

            Mesh var19 = null;
            FloatBuffer var20 = GlUtil.getDynamicByteBuffer(48, 0).asFloatBuffer();
            FloatBuffer var21 = GlUtil.getDynamicByteBuffer(64, 1).asFloatBuffer();
            GlUtil.glEnable(3553);

            for(var9 = 0; var9 < this.lodPointer; ++var9) {
                LodDraw var22;
                if (!(var22 = this.lodDraws[var9]).faulty) {
                    Mesh var25;
                    if ((var25 = var22.mesh) != var19) {
                        if (var2) {
                            GlUtil.printGlErrorCritical();
                        }

                        if (var19 != null) {
                            var19.unloadVBO(true);
                        }

                        var25.loadVBO(true);
                        var19 = var25;
                        if (var1) {
                            if (var18 == null) {
                                (var18 = ShaderLibrary.lodCubeShaderShadow).loadWithoutUpdate();
                            }
                        } else if (!var25.getMaterial().isMaterialBumpMapped()) {
                            if (var18 != ShaderLibrary.lodCubeShaderNormalOff) {
                                if (var18 != null) {
                                    var18.unloadWithoutExit();
                                }

                                (var18 = ShaderLibrary.lodCubeShaderNormalOff).loadWithoutUpdate();
                            }
                        } else {
                            if (!var25.hasTangents && (var18 == null || var18 == ShaderLibrary.lodCubeShaderNormalOff || var18 == ShaderLibrary.lodCubeShaderTangent)) {
                                if (var18 != null) {
                                    var18.unloadWithoutExit();
                                }

                                (var18 = ShaderLibrary.lodCubeShader).loadWithoutUpdate();
                            }

                            if (var25.hasTangents && (var18 == null || var18 == ShaderLibrary.lodCubeShaderNormalOff || var18 == ShaderLibrary.lodCubeShader)) {
                                if (var18 != null) {
                                    var18.unloadWithoutExit();
                                }

                                (var18 = ShaderLibrary.lodCubeShaderTangent).loadWithoutUpdate();
                            }
                        }

                        if (shader.shadowParams != null) {
                            shader.shadowParams.execute(var18);
                        }

                        if (var2) {
                            GlUtil.printGlErrorCritical();
                        }

                        GlUtil.glActiveTexture(33984);
                        GlUtil.glBindTexture(3553, var25.getMaterial().getTexture() != null ? var25.getMaterial().getTexture().getTextureId() : 0);
                        GlUtil.updateShaderVector3f(var18, "viewPos", Controller.getCamera().getPos());
                        GlUtil.updateShaderVector3f(var18, "lightPos", MainGameGraphics.mainLight.getPos());
                        GlUtil.updateShaderInt(var18, "mainTex", 0);
                        if (var25.getMaterial().isMaterialBumpMapped()) {
                            GlUtil.glActiveTexture(33985);
                            GlUtil.glBindTexture(3553, var25.getMaterial().getNormalMap().getTextureId());
                            GlUtil.updateShaderInt(var18, "normalTex", 1);
                        }

                        if (var2) {
                            GlUtil.printGlErrorCritical();
                        }

                        if (var25.getMaterial().getEmissiveTexture() != null) {
                            GlUtil.glActiveTexture(33986);
                            GlUtil.glBindTexture(3553, var25.getMaterial().getEmissiveTexture().getTextureId());
                            GlUtil.updateShaderInt(var18, "emissiveTex", 2);
                            GlUtil.updateShaderBoolean(var18, "emissiveOn", true);
                        } else {
                            GlUtil.updateShaderBoolean(var18, "emissiveOn", false);
                        }

                        if (var2) {
                            GlUtil.printGlErrorCritical();
                        }

                        GlUtil.glActiveTexture(33984);
                    }

                    if (var2) {
                        GlUtil.printGlErrorCritical();
                    }

                    GlUtil.glPushMatrix();
                    GlUtil.glMultMatrix(var22.transform);
                    GlUtil.glMultMatrix(var25.getParent().getTransform());
                    if (var2) {
                        GlUtil.printGlErrorCritical();
                    }

                    var22.fillLightBuffers(var20, var21);
                    if (var2) {
                        GlUtil.printGlErrorCritical();
                    }

                    GlUtil.updateShaderFloats3(var18, "lightVec", var20);
                    if (var2) {
                        GlUtil.printGlErrorCritical();
                    }

                    GlUtil.updateShaderFloats4(var18, "lightDiffuse", var21);
                    if (var2) {
                        GlUtil.printGlErrorCritical();
                    }

                    var25.renderVBO();
                    if (var2) {
                        GlUtil.printGlErrorCritical();
                    }

                    GlUtil.glPopMatrix();
                }
            }

            if (var19 != null) {
                var19.unloadVBO(true);
            }

            if (var2) {
                GlUtil.printGlErrorCritical();
            }

            if (var18 != null) {
                var18.unloadWithoutExit();
            }

            GlUtil.glActiveTexture(33984);
            GlUtil.glBindTexture(3553, 0);
            GlUtil.glActiveTexture(33985);
            GlUtil.glBindTexture(3553, 0);
            GlUtil.glActiveTexture(33986);
            GlUtil.glBindTexture(3553, 0);
            GlUtil.glActiveTexture(33984);
            if (var2) {
                GlUtil.printGlErrorCritical();
            }
        }

    }

    public boolean isInvisible() {
        return false;
    }

    public void onInit() {
        this.updateSegmentControllerSet();
        this.textBox = new AbstractTextBox(this.state);
        this.textBox.onInit();
        this.segmentLightingUpdate.start();
        this.segmentSorter.initialize();
        this.segmentSorter.start();
        this.textBoxes.initialize();
        this.d.generate((Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState());
        segmentOcclusion.initializeAABB();
        if (GraphicsContext.getCapabilities().GL_NVX_gpu_memory_info && !EngineSettings.USE_GL_MULTI_DRAWARRAYS_INITIAL_SET.isOn()) {
            EngineSettings.USE_GL_MULTI_DRAWARRAYS.setCurrentState(true);
            EngineSettings.USE_GL_MULTI_DRAWARRAYS_INITIAL_SET.setCurrentState(true);
        }

        this.textBoxFont = FontLibrary.getBoldArial18NoOutline();
        this.firstDraw = false;
    }

    public void cleanUpCubeMeshes() {
        dataPool.cleanUpGL();
    }

    public void clearSegmentControllers() {
        synchronized(this.segmentControllers) {
            this.segmentControllers.clear();
        }
    }

    public void completeVisUpdate(SegmentController var1) {
        var1.getSegmentBuffer().iterateOverEveryElement(new SegmentBufferIteratorEmptyInterface() {
            public boolean handleEmpty(int var1, int var2, int var3, long var4) {
                return true;
            }

            public boolean handle(Segment var1, long var2) {
                if (var1 != null) {
                    synchronized(SegmentDrawer.this.disposable) {
                        SegmentDrawer.this.disposable.add((DrawableRemoteSegment)var1);
                    }

                    ((DrawableRemoteSegment)var1).setNeedsMeshUpdate(true);
                    ((DrawableRemoteSegment)var1).lightTries = 0;
                }

                return true;
            }
        }, false);
    }

    public void contextSwitch(DrawableRemoteSegment var1, int var2) {
        assert var1.getCurrentBufferContainer() != null;

        System.currentTimeMillis();
        SegmentData var3;
        if ((var3 = var1.getSegmentData()) != null) {
            var3.loadLodFromContainer(var1.getCurrentBufferContainer());
        }

        var1.getNextCubeMesh().contextSwitch(var1.getCurrentBufferContainer(), var1, var2);
        this.state.getWorldDrawer().getFlareDrawerManager().updateSegment(var1);
        System.currentTimeMillis();
    }

    public int drawSegmentController(SegmentController var1, Shader var2) {
        if (var1 != null && var1.isInClientRange()) {
            int var3 = Math.min(this.max, this.drawnSegmentsPointer);
            this.beforeShift.set(-1000000.0F, -1000000.0F, 1000000.0F);
            this.culling = true;
            System.nanoTime();
            System.currentTimeMillis();
            this.lastTransform.set(-1, 0, 0);
            GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glClear(16384);
            CubeData.resetDrawn();
            this.filterDrawn = 0;
            if (seperateDrawing) {
                try {
                    this.modelview.load(Controller.modelviewMatrix);
                    this.filter = var1;
                    this.prepareDraw(var2, shader);
                    this.setSegmentRenderPass(SegmentDrawer.SegmentRenderPass.ALL);
                    this.drawSeperated(var3, System.currentTimeMillis(), var2, false, false);
                    this.endDraw(var2);
                    this.setSegmentRenderPass((SegmentDrawer.SegmentRenderPass)null);
                } finally {
                    this.filter = null;
                }
            }

            return this.filterDrawn;
        } else {
            return 0;
        }
    }

    private void drawSeperated(int var1, long var2, Shader var4, boolean var5, boolean var6) {
        boolean var7 = !Keyboard.isKeyDown(65);
        boolean var8 = var4 == ShaderLibrary.getCubeShader(this.getState().getWorldDrawer().isSpotLightSupport());
        boolean var9 = var4 == ShaderLibrary.getCubeShader(this.getState().getWorldDrawer().isSpotLightSupport() | CubeShaderType.LIGHT_ALL.bit);
        boolean var10 = Shadow.creatingMap;
        Shader var11 = var4;
        if (this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.ALL || this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.OPAQUE) {
            GlUtil.enableBlend(false);
            GlUtil.glDisable(3042);

            long var12;
            long var14;
            try {
                var12 = System.currentTimeMillis();
                this.markFarAndDrawNearOpaqueSeperated(var1, var2, var9);
                if ((var14 = System.currentTimeMillis() - var12) > 50L) {
                    System.err.println("DRAWING TIME WARNING 0: " + var14);
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }

            var12 = System.currentTimeMillis();
            var11 = this.seperatedOpaqueStage(var1, var7, var8, var10, var9, false, var2, var4, var5, var6, true);
            if ((var14 = System.currentTimeMillis() - var12) > 50L) {
                System.err.println("DRAWING TIME WARNING 1: " + var14);
            }
        }

        Shader var19 = var4;
        if (var8) {
            var11.unload();
            (var19 = ShaderLibrary.getCubeShader(CubeShaderType.VERTEX_LIGHTING.bit | CubeShaderType.BLENDED.bit)).setShaderInterface(var4.getShaderInterface());
            var19.load();
        } else if (var9) {
            var11.unload();
            (var19 = ShaderLibrary.getCubeShader(CubeShaderType.VERTEX_LIGHTING.bit | CubeShaderType.LIGHT_ALL.bit | CubeShaderType.BLENDED.bit)).setShaderInterface(var4.getShaderInterface());
            var19.load();
        } else if (var10) {
            var4.unload();
            (var19 = Shadow.getShadowShader(true)).setShaderInterface(var4.getShaderInterface());
            var19.load();
        }

        if (this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.ALL || this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.TRANSPARENT) {
            GlUtil.enableBlend(true);
            this.enableBlend();
            long var13 = System.currentTimeMillis();
            this.markSeperatedBySegBlend(var1, var2, var9);
            long var15;
            if ((var15 = System.currentTimeMillis() - var13) > 50L) {
                System.err.println("DRAWING TIME WARNING 2: " + var15);
            }

            var13 = System.currentTimeMillis();
            CubeData.manager.drawMulti(true, var19);
            if ((var15 = System.currentTimeMillis() - var13) > 50L) {
                System.err.println("DRAWING TIME WARNING 3: " + var15);
            }

            shader.quality = CubeTexQuality.SELECTED;
            Shader var18;
            if (var8) {
                var19.unload();
                (var18 = ShaderLibrary.getCubeShader(this.getState().getWorldDrawer().isSpotLightSupport() | CubeShaderType.BLENDED.bit)).setShaderInterface(var4.getShaderInterface());
                var18.load();
                var13 = System.currentTimeMillis();
                this.drawSeperatedSortedBlend(var1, var2, var18, var9, var5, var6);
                if ((var15 = System.currentTimeMillis() - var13) > 50L) {
                    System.err.println("DRAWING TIME WARNING 4: " + var15);
                }
            } else if (var9) {
                var19.unload();
                (var18 = ShaderLibrary.getCubeShader(this.getState().getWorldDrawer().isSpotLightSupport() | CubeShaderType.LIGHT_ALL.bit | CubeShaderType.BLENDED.bit)).setShaderInterface(var4.getShaderInterface());
                var18.load();
                var13 = System.currentTimeMillis();
                this.drawSeperatedSortedBlend(var1, var2, var18, var9, var5, var6);
                if ((var15 = System.currentTimeMillis() - var13) > 50L) {
                    System.err.println("DRAWING TIME WARNING 5: " + var15);
                }
            } else {
                var13 = System.currentTimeMillis();
                this.drawSeperatedSortedBlend(var1, var2, var4, var9, var5, var6);
                if ((var15 = System.currentTimeMillis() - var13) > 50L) {
                    System.err.println("DRAWING TIME WARNING 6: " + var15);
                }
            }

            if (var10 || var8 || var9) {
                var4.load();
            }
        }

        this.setSegmentRenderPass((SegmentDrawer.SegmentRenderPass)null);
    }

    private Shader seperatedOpaqueStage(int var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, long var7, Shader var9, boolean var10, boolean var11, boolean var12) {
        shader.quality = CubeTexQuality.SELECTED;
        long var13 = System.currentTimeMillis();
        GlUtil.glPushMatrix();
        this.drawSeperatedSortedOpaque(var1, var7, var9, var5, var10, var11);
        GlUtil.glPopMatrix();
        long var15;
        if ((var15 = System.currentTimeMillis() - var13) > 50L) {
            System.err.println("DRAWING TIME WARNING SOS1: " + var15);
        }

        if (var2) {
            shader.quality = CubeTexQuality.LOW;
        }

        Shader var17 = null;
        if (!var6) {
            if (var5) {
                var9.unload();
                (var17 = ShaderLibrary.getCubeShader(CubeShaderType.VERTEX_LIGHTING.bit | CubeShaderType.LIGHT_ALL.bit)).setShaderInterface(var9.getShaderInterface());
                var17.load();
            } else if (var3) {
                var9.unload();
                (var17 = ShaderLibrary.getCubeShader(CubeShaderType.VERTEX_LIGHTING.bit)).setShaderInterface(var9.getShaderInterface());
                var17.load();
            }
        }

        var13 = System.currentTimeMillis();
        CubeData.manager.drawMulti(var12, var17);
        if ((var15 = System.currentTimeMillis() - var13) > 50L) {
            System.err.println("DRAWING TIME WARNING SOS2: " + var15);
        }

        return var17;
    }

    private void markFarAndDrawNearOpaqueSeperated(int var1, long var2, boolean var4) {
        int var5 = Keyboard.isKeyDown(KeyboardMappings.PLAYER_LIST.getMapping()) && Keyboard.isKeyDown(54) && Keyboard.isKeyDown(203) ? 1 : 63;
        boolean var6 = Keyboard.isKeyDown(KeyboardMappings.PLAYER_LIST.getMapping()) && Keyboard.isKeyDown(208);
        this.blendedPointer = 0;
        this.blendedPointerBySeg = 0;
        this.opaquePointer = 0;

        for(int var7 = 0; var7 < var1; ++var7) {
            DrawableRemoteSegment var8 = this.drawnSegmentsBySegmentController[var7];
            DrawableRemoteSegment var9 = this.drawnSegments[var7];
            if (var8 != null && var9 != null && (this.filter == null || this.filter == var8.getSegmentController() || this.filter == var9.getSegmentController())) {
                boolean var10;
                if ((var10 = !var9.getSegmentController().isInvisibleNextDraw() && !var9.isEmpty() && this.inViewFrustum(var9) && var9.getCurrentCubeMesh() != null) && var9.lastSegmentDistSquared < 10000.0F) {
                    if (var6 && var9.getSegmentData() instanceof SegmentDataIntArray) {
                        continue;
                    }

                    if (this.filter == null || this.filter == var9.getSegmentController()) {
                        this.drawnOpaqueSegments[this.opaquePointer] = var9;
                        ++this.opaquePointer;
                    }
                }

                boolean var11;
                if (var11 = !var8.getSegmentController().isInvisibleNextDraw() && !var8.isEmpty() && !(!this.checkNeedsMeshUpdate(var8, var2) | !var8.isActive()) && this.inViewFrustum(var8)) {
                    if (var6 && var8.getSegmentData() instanceof SegmentDataIntArray) {
                        continue;
                    }

                    TransformTimed var12 = var8.getSegmentController().getWorldTransformOnClient();
                    if (var8.lastSegmentDistSquared >= 10000.0F) {
                        byte var13 = 0;
                        if (this.isDrawVirtual(var8, var4)) {
                            var13 = 1;
                        }

                        if (this.filter == null || this.filter == var8.getSegmentController()) {
                            ++this.filterDrawn;
                            ((CubeMeshDynOpt)var8.getCurrentCubeMesh().cubeMesh).mark(var12, var8.getSegmentController().getId(), var13, false, var5);
                        }
                    }

                    var8.getCurrentCubeMesh().getBlendedElementsCount();
                }

                if (var11 && var8.lastSegmentDistSquared >= 900.0F) {
                    if (var6 && var8.getSegmentData() instanceof SegmentDataIntArray) {
                        continue;
                    }

                    if (this.filter == null || this.filter == var8.getSegmentController()) {
                        ++this.filterDrawn;
                        this.drawnBlendedSegmentsBySegment[this.blendedPointerBySeg] = var8;
                        ++this.blendedPointerBySeg;
                    }
                }

                if (var10 && var9.lastSegmentDistSquared < 900.0F && (this.filter == null || this.filter == var9.getSegmentController()) && (!var6 || !(var9.getSegmentData() instanceof SegmentDataIntArray))) {
                    ++this.filterDrawn;
                    this.drawnBlendedSegments[this.blendedPointer] = var9;
                    ++this.blendedPointer;
                }
            }
        }

    }

    private boolean isDrawVirtual(DrawableRemoteSegment var1, boolean var2) {
        return var1.getSegmentController().isVirtualBlueprint() && (!var2 || var1.getSegmentController() != this.state.getShip()) && var1.getSegmentController().percentageDrawn >= 1.0F;
    }

    private void drawSeperatedSortedOpaque(int var1, long var2, Shader var4, boolean var5, boolean var6, boolean var7) {
        this.currentSegmentController = null;
        Shader var13 = var4;
        Shader var8 = var4;
        Shader var9 = null;
        if (var4.optionBits >= 0) {
            (var9 = ShaderLibrary.getCubeShader(var4.optionBits | CubeShaderType.VIRTUAL.bit)).setShaderInterface(var4.getShaderInterface());
        }

        for(int var10 = this.opaquePointer - 1; var10 >= 0; --var10) {
            DrawableRemoteSegment var11 = this.drawnOpaqueSegments[var10];
            if (this.filter == null || this.filter == var11.getSegmentController()) {
                ++this.filterDrawn;
                if (var9 != null) {
                    boolean var12;
                    if ((var12 = this.isDrawVirtual(var11, var5)) && var13 != var9) {
                        var8.unload();
                        var9.load();
                        var13 = var9;
                        this.currentSegmentController = null;
                        this.beforeShift.set(-1000000.0F, -1000000.0F, 1000000.0F);
                    } else if (!var12 && var13 != var8) {
                        var9.unload();
                        var8.load();
                        var13 = var8;
                        this.currentSegmentController = null;
                        this.beforeShift.set(-1000000.0F, -1000000.0F, 1000000.0F);
                    }
                } else {
                    assert var4 == ShaderLibrary.depthCubeShader || EngineSettings.G_SHADOWS.isOn() || this.filter != null;
                }

                assert !this.isDrawVirtual(var11, var5) || GlUtil.loadedShader.optionBits < 0 || (GlUtil.loadedShader.optionBits & CubeShaderType.VIRTUAL.bit) == CubeShaderType.VIRTUAL.bit : ((GlUtil.loadedShader.optionBits & CubeShaderType.VIRTUAL.bit) == CubeShaderType.VIRTUAL.bit) + "; " + ((var13.optionBits & CubeShaderType.VIRTUAL.bit) == CubeShaderType.VIRTUAL.bit);

                int var14 = Keyboard.isKeyDown(KeyboardMappings.PLAYER_LIST.getMapping()) && Keyboard.isKeyDown(54) && Keyboard.isKeyDown(203) ? 21 : 63;
                this.draw(var11, 0, var2, var13, var6, var7, false, var14);
            }
        }

        if (var13 == var9) {
            var9.unload();
            var8.load();
        }

    }

    private void markSeperatedBySegBlend(int var1, long var2, boolean var4) {
        var1 = Keyboard.isKeyDown(KeyboardMappings.PLAYER_LIST.getMapping()) && Keyboard.isKeyDown(54) && Keyboard.isKeyDown(203) ? 1 : 63;

        for(int var7 = 0; var7 < this.blendedPointerBySeg; ++var7) {
            DrawableRemoteSegment var3 = this.drawnBlendedSegmentsBySegment[var7];
            if (this.filter == null || var3.getSegmentController() == this.filter) {
                ++this.filterDrawn;
                TransformTimed var5 = var3.getSegmentController().getWorldTransformOnClient();
                byte var6 = 0;
                if (this.isDrawVirtual(var3, var4)) {
                    var6 = 1;
                }

                ((CubeMeshDynOpt)var3.getCurrentCubeMesh().cubeMesh).mark(var5, var3.getSegmentController().getId(), var6, true, var1);
            }
        }

    }

    private void drawSeperatedSortedBlend(int var1, long var2, Shader var4, boolean var5, boolean var6, boolean var7) {
        var1 = Keyboard.isKeyDown(KeyboardMappings.PLAYER_LIST.getMapping()) && Keyboard.isKeyDown(54) && Keyboard.isKeyDown(203) ? 1 : 63;
        this.currentSegmentController = null;

        for(int var8 = this.blendedPointer - 1; var8 >= 0; --var8) {
            DrawableRemoteSegment var3 = this.drawnBlendedSegments[var8];
            if (this.filter == null || this.filter == var3.getSegmentController()) {
                ++this.filterDrawn;
                TransformTimed var9 = var3.getSegmentController().getWorldTransformOnClient();
                byte var10 = 0;
                if (this.isDrawVirtual(var3, var5)) {
                    var10 = 1;
                }

                ((CubeMeshDynOpt)var3.getCurrentCubeMesh().cubeMesh).mark(var9, var3.getSegmentController().getId(), var10, true, var1);
            }
        }

        CubeData.manager.drawMulti(true, var4);
    }

    public void draw(Shaderable var1, Shader var2, boolean var3, boolean var4, SegmentOcclusion var5, short var6) {
        this.beforeShift.set(-1000000.0F, -1000000.0F, 1000000.0F);
        this.effect = null;
        this.currentDrawing = null;
        this.lodShapes.clear();
        this.updateNum = var6;
        BeamState var25 = null;
        ObjectHeapPriorityQueue var26 = this.state.getWorldDrawer().getBeamDrawerManager().getSortedStates();
        LOD_THRESH_SQUARED = (float)(((double)(Float)EngineSettings.LOD_DISTANCE_IN_THRESHOLD.getCurrentState() + 16.0D) * ((double)(Float)EngineSettings.LOD_DISTANCE_IN_THRESHOLD.getCurrentState() + 16.0D));
        boolean var7 = EngineSettings.G_DRAW_BEAMS.isOn() && (this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.ALL || this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.TRANSPARENT);
        long var9 = System.currentTimeMillis();
        Keyboard.isKeyDown(29);
        Vector3f var8 = Controller.getCamera().getPos();
        if (var7 && !var26.isEmpty()) {
            var25 = (BeamState)var26.dequeue();
        }

        if (var7 && (!var26.isEmpty() || var25 != null)) {
            if (Keyboard.isKeyDown(60)) {
                GlUtil.printGlErrorCritical();
            }

            this.state.getWorldDrawer().getBeamDrawerManager().prepareDraw(0.0F);
            if (Keyboard.isKeyDown(60)) {
                GlUtil.printGlErrorCritical();
            }

            GlUtil.glBlendFunc(770, 1);
            if (var25 != null) {
                BeamDrawer.drawConnection(var25, beamTmp, var9, var8);
            }

            while(!var26.isEmpty()) {
                BeamDrawer.drawConnection((BeamState)var26.dequeue(), beamTmp, var9, var8);
            }

            if (Keyboard.isKeyDown(60)) {
                GlUtil.printGlErrorCritical();
            }

            this.state.getWorldDrawer().getBeamDrawerManager().endDraw();
            GlUtil.glBlendFunc(770, 771);
            if (Keyboard.isKeyDown(60)) {
                GlUtil.printGlErrorCritical();
            }
        }

        this.frustumCulling = EngineSettings.G_FRUSTUM_CULLING.isOn() && this.culling;
        seperateDrawing = !Keyboard.isKeyDown(67);
        System.currentTimeMillis();
        this.ff = true;
        if (this.firstDraw) {
            this.onInit();
        }

        if (this.max != 0 && this.max != (Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState()) {
            this.flagMaxChanged = true;
        }

        if (this.flagMaxChanged && !this.state.getGlobalGameControlManager().getOptionsControlManager().isTreeActive()) {
            System.err.println("[SEGMENTDRAWER] Changed max drawn -> " + (Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState());
            this.state.getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_SEGMENTDRAWER_0, 0.0F);
            synchronized(this.segmentLightingUpdate.lightingUpdates) {
                if (this.segmentLightingUpdate.lightingUpdates.size() > 0) {
                    System.err.println("LIGHT QUEUE: " + this.segmentLightingUpdate.lightingUpdates.size());
                    this.segmentLightingUpdate.lightingUpdates.notify();
                } else {
                    synchronized(this.segmentControllers) {
                        Iterator var29 = this.segmentControllers.iterator();

                        while(true) {
                            if (!var29.hasNext()) {
                                break;
                            }

                            SegmentController var31 = (SegmentController)var29.next();
                            this.completeVisUpdate(var31);
                        }
                    }

                    this.d.generate((Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState());
                    segmentOcclusion.setInitialized(false);
                    segmentOcclusion.reinitialize((Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState() << 1);
                    if (Shadow.occlusions != null) {
                        for(int var27 = 0; var27 < Shadow.occlusions.length; ++var27) {
                            Shadow.occlusions[var27].setInitialized(false);
                            Shadow.occlusions[var27].reinitialize((Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState() << 1);
                        }
                    }

                    this.afterResort();
                    dataPool.cleanUpGL();
                    dataPool = new CubeDataPool();
                    synchronized(this.segmentSorter) {
                        this.drawnSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnBlendedSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnOpaqueSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnBlendedSegmentsBySegment = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnSegmentsDouble = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.deactivatedSegments = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.deactivatedSegmentsDouble = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnSegmentsBySegmentController = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnSegmentsBySegmentControllerDouble = new DrawableRemoteSegment[dataPool.POOL_SIZE];
                        this.drawnSegmentsPointer = 0;
                    }

                    this.max = (Integer)EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState();
                    this.flagMaxChanged = false;
                    this.state.getController().popupInfoTextMessage("#SegmentDrawn count successfully\nupdated to " + this.max, 0.0F);
                }
            }
        }

        if (forceFullLightingUpdate) {
            System.err.println("Executing FULL vis update");
            synchronized(this.segmentControllers) {
                Iterator var28 = this.segmentControllers.iterator();

                while(var28.hasNext()) {
                    SegmentController var30 = (SegmentController)var28.next();
                    this.completeVisUpdate(var30);
                }
            }
        }

        boolean var10000 = reinitializeMeshes;
        System.currentTimeMillis();
        GameClientState.avgBlockLightTime = this.segmentLightingUpdate.getAvgHandleTime();
        GameClientState.avgBlockLightLockTime = this.segmentLightingUpdate.getAvgSegLockTime();
        if (System.currentTimeMillis() - this.segmentLightingUpdate.t > 1000L) {
            this.segmentLightingUpdate.t = System.currentTimeMillis();
            this.segmentLightingUpdate.updatesPerSecond = this.segmentLightingUpdate.updates;
            this.segmentLightingUpdate.updates = 0;
        }

        AbstractScene.infoList.add("CONTEXT UPDATES: " + this.segmentLightingUpdate.updatesPerSecond + "; enqueued: " + this.segmentLightingUpdate.lightingUpdates.size());
        this.timeDrawn = System.currentTimeMillis();
        System.currentTimeMillis();
        this.prepareDraw(var2, var1);
        this.stats.timeForFrustum = 0L;
        this.stats.timeForCheckUpdate = 0L;
        this.stats.timeForDrawVisible = 0L;
        this.stats.timeForUniforms = 0L;
        this.stats.timeForTotalDraw = 0L;
        this.stats.timeForActualDraw = 0L;
        this.modelview.load(Controller.modelviewMatrix);
        GlUtil.glPushMatrix();
        this.stats.drawnBoxes = 0L;
        boolean var32 = false;
        this.sorterUpdate = false;
        long var11 = System.currentTimeMillis();
        synchronized(this.drawnSegments) {
            int var33 = Math.min(this.max, this.drawnSegmentsPointer);
            long var13 = System.nanoTime();
            long var15 = System.currentTimeMillis();
            this.lastTransform.set(-1, 0, 0);
            CubeData.resetDrawn();
            if (seperateDrawing) {
                this.drawSeperated(var33, var11, var2, var3, var4);
            }

            int var24 = (int)(System.currentTimeMillis() - var15);
            long var17;
            if ((float)(var17 = System.nanoTime() - var13) / 1000000.0F > 30.0F) {
                System.err.println("[SEGMENT_DRAWER] DRAWING TIME OF " + var33 + " elements: " + var24 + "(" + (float)var17 / 1000000.0F + "); unifroms: " + (float)this.stats.timeForUniforms / 1000000.0F + "; pointer " + (float)this.stats.timeForDrawVisible / 1000000.0F + "; upChk " + (float)this.stats.timeForCheckUpdate / 1000000.0F + "; frust " + (float)this.stats.timeForFrustum / 1000000.0F + "; update " + (float)this.stats.meshUpdateTime / 1000000.0F + "; draw: " + (float)this.stats.timeForActualDraw / 1000000.0F + "; totD: " + (float)this.stats.timeForTotalDraw / 1000000.0F);
            }
        }

        this.endDraw(var2);
        System.currentTimeMillis();
        GlUtil.glPopMatrix();
        System.currentTimeMillis();
        if (System.currentTimeMillis() - this.stats.lastLightQueueInfoUpdate > 1000L) {
            this.stats.lastLightQueueInfoUpdate = System.currentTimeMillis();
        }

        if (Keyboard.isKeyDown(60)) {
            GlUtil.printGlErrorCritical();
        }

    }

    public boolean draw(DrawableRemoteSegment var1, int var2, long var3, Shader var5, boolean var6, boolean var7, boolean var8, int var9) {
        if (var1.getSegmentController().isInvisibleNextDraw()) {
            return false;
        } else {
            SegmentData var15;
            if (this.bb % 10 == 0 && EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn() && EngineSettings.P_PHYSICS_DEBUG_ACTIVE_OCCLUSION.isOn()) {
                assert false;

                if (var1.isEmpty()) {
                    DebugBox var10 = new DebugBox(new Vector3f((float)(var1.pos.x - 16), (float)(var1.pos.y - 16), (float)(var1.pos.z - 16)), new Vector3f((float)(var1.pos.x + 16), (float)(var1.pos.y + 16), (float)(var1.pos.z + 16)), var1.getSegmentController().getWorldTransformOnClient(), 0.0F, 1.0F, 0.0F, 1.0F);
                    DebugDrawer.boxes.add(var10);
                } else {
                    var15 = var1.getSegmentData();
                    DebugBox var11;
                    if (!var1.isActive()) {
                        var11 = new DebugBox(new Vector3f((float)(var15.getSegment().pos.x - 16), (float)(var15.getSegment().pos.y - 16), (float)(var15.getSegment().pos.z - 16)), new Vector3f((float)(var15.getSegment().pos.x + 16), (float)(var15.getSegment().pos.y + 16), (float)(var15.getSegment().pos.z + 16)), var1.getSegmentController().getWorldTransformOnClient(), 0.0F, 0.0F, 1.0F, 1.0F);
                        DebugDrawer.boxes.add(var11);
                    } else if (var1.occlusionFailed) {
                        var11 = new DebugBox(new Vector3f((float)(var15.getSegment().pos.x - 16), (float)(var15.getSegment().pos.y - 16), (float)(var15.getSegment().pos.z - 16)), new Vector3f((float)(var15.getSegment().pos.x + 16), (float)(var15.getSegment().pos.y + 16), (float)(var15.getSegment().pos.z + 16)), var1.getSegmentController().getWorldTransformOnClient(), 1.0F, 0.0F, 0.0F, 1.0F);
                        DebugDrawer.boxes.add(var11);
                    } else {
                        var11 = new DebugBox(new Vector3f((float)(var15.getSegment().pos.x - 16), (float)(var15.getSegment().pos.y - 16), (float)(var15.getSegment().pos.z - 16)), new Vector3f((float)(var15.getSegment().pos.x + 16), (float)(var15.getSegment().pos.y + 16), (float)(var15.getSegment().pos.z + 16)), var1.getSegmentController().getWorldTransformOnClient(), 1.0F, 1.0F, 1.0F, 1.0F);
                        DebugDrawer.boxes.add(var11);
                    }
                }
            }

            ++this.bb;
            if (var1.isEmpty()) {
                return false;
            } else if (!this.checkNeedsMeshUpdate(var1, var3) | !var1.isActive()) {
                return false;
            } else {
                var1.lastDrawn = this.timeDrawn;
                if (this.currentDrawing != var1.getSegmentController()) {
                    this.effect = this.state.getWorldDrawer().getSegmentControllerEffectDrawer().getEffect(var1.getSegmentController());
                    this.currentDrawing = var1.getSegmentController();
                }

                if (this.effect == null && SegmentControllerEffectDrawer.unaffectedTranslation == null && !this.inViewFrustum(var1)) {
                    return false;
                } else {
                    int var17;
                    if (this.currentSegmentController != var1.getSegmentController()) {
                        if (!directModelview) {
                            GlUtil.glPopMatrix();
                            GlUtil.glPushMatrix();
                            this.loadTransform(var1.getSegmentController());
                        }

                        this.lastTransform.set(-1, 0, 0);
                        this.currentSegmentController = var1.getSegmentController();
                        if (!var8 && var5.optionBits >= 0 && (var5.optionBits & CubeShaderType.LIGHT_ALL.bit) == CubeShaderType.LIGHT_ALL.bit) {
                            BuildToolsManager var16 = this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getBuildToolsManager();
                            if (this.turnOffAllLight) {
                                GlUtil.updateShaderInt(var5, "allLight", 0);
                                this.turnOffAllLight = false;
                            }

                            if (var5.optionBits >= 0 && (var5.optionBits & CubeShaderType.LIGHT_ALL.bit) == CubeShaderType.LIGHT_ALL.bit && this.state.getCurrentPlayerObject() == this.currentSegmentController) {
                                PlayerGameControlManager var19;
                                var7 = (var19 = this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager()).getPlayerIntercationManager().getSegmentControlManager().getSegmentBuildController().isTreeActive() || var19.getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getSegmentBuildController().isTreeActive();
                                var17 = var16.lighten && !var19.getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getShipExternalFlightController().isTreeActive() ? 2 : 0;
                                if (var7 && !EngineSettings.G_DRAW_NO_OVERLAYS.isOn()) {
                                    ++var17;
                                }

                                GlUtil.updateShaderInt(var5, "allLight", var17);
                                this.turnOffAllLight = true;
                            }
                        }
                    }

                    if (this.currentSegmentController.blinkTime > 0L) {
                        long var18;
                        if ((var18 = var3 - this.currentSegmentController.blinkTime) < 1200L) {
                            this.blinkingTime = var18;
                            GlUtil.updateShaderFloat(var5, "selectTime", (float)var18);
                            if (this.currentSegmentController.blinkShader == null) {
                                this.currentSegmentController.blinkShader = var5;
                            }
                        } else if (this.currentSegmentController.blinkShader != null) {
                            var5.unload();
                            Iterator var13 = ShaderLibrary.getCubeShaders().iterator();

                            while(var13.hasNext()) {
                                Shader var21;
                                (var21 = (Shader)var13.next()).loadWithoutUpdate();
                                GlUtil.updateShaderFloat(var21, "selectTime", 0.0F);
                                var21.unloadWithoutExit();
                            }

                            this.currentSegmentController.blinkTime = 0L;
                            this.currentSegmentController.blinkShader = null;
                            var5.load();
                        }
                    } else if (this.blinkingTime > 0L) {
                        var5.unload();
                        Iterator var22 = ShaderLibrary.getCubeShaders().iterator();

                        while(var22.hasNext()) {
                            Shader var20;
                            (var20 = (Shader)var22.next()).loadWithoutUpdate();
                            GlUtil.updateShaderFloat(var20, "selectTime", 0.0F);
                            var20.unloadWithoutExit();
                        }

                        var5.load();
                        this.blinkingTime = 0L;
                    }

                    var17 = ByteUtil.divU256(ByteUtil.divUSeg(var1.pos.x) + 128);
                    int var23 = ByteUtil.divU256(ByteUtil.divUSeg(var1.pos.y) + 128);
                    int var14 = ByteUtil.divU256(ByteUtil.divUSeg(var1.pos.z) + 128);
                    this.afterShift.set((float)var17, (float)var23, (float)var14);
                    this.afterShift.scale(256.0F);
                    if (!this.beforeShift.equals(this.afterShift)) {
                        GlUtil.updateShaderVector3f(var5, "shift", this.afterShift);
                        this.beforeShift.set(this.afterShift);
                    }

                    if ((var15 = var1.getSegmentData()) != null && var15.drawingLodShapes != null && var15.drawingLodShapes.size() > 0 && var1.lastSegmentDistSquared < LOD_THRESH_SQUARED) {
                        this.lodShapes.add(var15);
                    }

                    if (var1.getCurrentCubeMesh() != null) {
                        if (var8 || CubeMeshBufferContainer.vertexComponents < 3) {
                            if (this.lastTransform.x == -1) {
                                GL11.glTranslatef((float)var1.pos.x, (float)var1.pos.y, (float)var1.pos.z);
                            } else {
                                GL11.glTranslatef((float)(-this.lastTransform.x + var1.pos.x), (float)(-this.lastTransform.y + var1.pos.y), (float)(-this.lastTransform.z + var1.pos.z));
                            }
                        }

                        if (this.effect != null) {
                            GlUtil.glPushMatrix();
                            this.effect.modifyModelview(this.state);
                        }

                        if (this.effect == null || this.effect.isDrawOriginal()) {
                            if (var8) {
                                GL11.glDrawArrays(7, 0, 24);
                            } else {
                                var1.getCurrentCubeMesh().draw(var2, var9);
                            }
                        }

                        if (this.effect != null) {
                            GlUtil.glPopMatrix();
                        }

                        ++GameClientState.drawnSegements;
                        if (!var8 && var6) {
                            ShieldDrawer var12;
                            if ((var12 = this.state.getWorldDrawer().getShieldDrawerManager().get(var1.getSegmentController())) != null && var12.hasHit(var1) && ShaderLibrary.cubeShieldShader != null && var12.getShieldShader() != null) {
                                var5.unload();
                                ShaderLibrary.cubeShieldShader.setShaderInterface(var12.getShieldShader());
                                ShaderLibrary.cubeShieldShader.load();
                                var1.getCurrentCubeMesh().draw(var2, var9);
                                ShaderLibrary.cubeShieldShader.unload();
                                var5.load();
                            }

                            if (this.effect != null) {
                                GlUtil.glPushMatrix();
                                this.effect.modifyModelview(this.state);
                                this.effect.loadShader();
                                var1.getCurrentCubeMesh().draw(this.effect.overlayBlendMode(), var9);
                                this.effect.unloadShader();
                                GlUtil.glPopMatrix();
                                var5.load();
                            }
                        }

                        this.lastTransform.set(var1.pos);
                    }

                    this.stats.drawnBoxes = var1.isEmpty() ? 0L : (long)var1.getSize();
                    return var1.getCurrentCubeMesh().getBlendedElementsCount() > 0;
                }
            }
        }
    }

    public void drawTextBoxes() {
        ShaderLibrary.scanlineShader.setShaderInterface(this.textBox);
        ShaderLibrary.scanlineShader.load();
        synchronized(this.drawnSegments) {
            this.textBox.draw(this.textBoxes);
        }

        ShaderLibrary.scanlineShader.unload();
    }

    private void endDraw(Shader var1) {
        GlUtil.glDisableClientState(32884);
        this.currentSegmentController = null;
        GlUtil.glDisable(3042);
        GL11.glShadeModel(7425);
        GlUtil.glBindBuffer(34962, 0);
        var1.unload();
        if (GraphicsContext.INTEGER_VERTICES) {
            GL20.glDisableVertexAttribArray(0);
        }

    }

    public void forceAdd(DrawableRemoteSegment var1) {
        synchronized(this.drawnSegments) {
            if (this.calcInViewFrustum(var1) && this.drawnSegmentsPointer < this.drawnSegments.length) {
                var1.segmentBufferAABBHelper = var1.getSegmentBufferRegion();
                this.drawnSegments[this.drawnSegmentsPointer] = var1;
                this.drawnSegmentsBySegmentController[this.drawnSegmentsPointer] = var1;
                ++this.drawnSegmentsPointer;
            }

        }
    }

    public List<DrawableRemoteSegment> getRemovedSegments() {
        return this.removedSegments;
    }

    public void handleContextSwitches() {
        synchronized(this.generatedSegments) {
            for(int var2 = 0; var2 < this.generatedSegments.size(); ++var2) {
                //ins. decomp error
                DrawableRemoteSegment var3 = generatedSegments.get(var2);
                //
                assert var3.getNextCubeMesh() != null : var3.pos;

                assert var3.getNextCubeMesh() != null : var3.pos;

                synchronized(var3.cubeMeshLock) {
                    if (!var3.occlusionFailed || var3.getCurrentCubeMesh() == null) {
                        this.contextSwitch(var3, var2);
                    }

                    var3.releaseContainerFromPool();
                    if (var3.occlusionFailed && var3.getCurrentCubeMesh() != null) {
                        var3.keepOld();
                    } else {
                        var3.applyCurrent();
                    }

                    var3.setActive(true);
                    var3.setInUpdate(false);
                }

                this.afterGenerated.add(var3);
            }

            this.generatedSegments.clear();
            GlUtil.glBindBuffer(34962, 0);
        }
    }

    boolean inViewFrustum(DrawableRemoteSegment var1) {
        if (!this.frustumCulling) {
            return true;
        } else {
            if (var1.segmentBufferAABBHelper.aabbHelperUpdateNum != this.updateNum) {
                var1.segmentBufferAABBHelper.inViewFrustum = var1.segmentBufferAABBHelper.isInViewFrustum(this.minSBBBOut, this.maxSBBBOut, this.minSBBBOutC, this.maxSBBBOutC);
                var1.segmentBufferAABBHelper.inViewFrustumFully = var1.segmentBufferAABBHelper.isFullyInViewFrustum(this.minSBBBOut, this.maxSBBBOut, this.minSBBBOutC, this.maxSBBBOutC);
                var1.segmentBufferAABBHelper.aabbHelperUpdateNum = this.updateNum;
            }

            return var1.segmentBufferAABBHelper.inViewFrustumFully || var1.segmentBufferAABBHelper.inViewFrustum && this.calcInViewFrustum(var1);
        }
    }

    private boolean calcInViewFrustum(DrawableRemoteSegment var1) {
        var1.getAABBClient(this.minBBOut, this.maxBBOut, this.posOut);
        this.posOut.sub(Controller.getCamera().getPos());
        var1.lastSegmentDistSquared = this.posOut.lengthSquared();
        var1.cachedFrustum = var1.isInViewFrustum(this.updateNum);
        return var1.cachedFrustum;
    }

    public boolean isRecreate() {
        return this.recreate;
    }

    public void setRecreate(boolean var1) {
        this.recreate = var1;
    }

    private void loadTransform(SegmentController var1) {
        GlUtil.glMultMatrix(var1.getWorldTransformOnClient());
    }

    public void onRemovedSegmentController(SegmentController var1) {
    }

    private void prepareDraw(Shader var1, Shaderable var2) {
        var1.setShaderInterface(var2);
        var1.load();
        GlUtil.glEnable(2929);
        if (this.isCullFace()) {
            GlUtil.glEnable(2884);
        } else {
            GlUtil.glDisable(2884);
        }

        if (!seperateDrawing) {
            this.enableBlend();
        } else if (this.getSegmentRenderPass() == SegmentDrawer.SegmentRenderPass.OPAQUE) {
            GlUtil.glDisable(3042);
        } else {
            GlUtil.glEnable(3042);
        }

        this.stats.meshUpdateTime = 0L;
        GlUtil.glEnableClientState(32884);
        if (GraphicsContext.INTEGER_VERTICES) {
            GL20.glEnableVertexAttribArray(0);
        }

    }

    private void enableBlend() {
        GlUtil.glEnable(3042);
        if (blendFunc == 1) {
            GlUtil.glBlendFunc(1, 1);
            GlUtil.glBlendFuncSeparate(1, 1, 1, 1);
        } else if (blendFunc == 2) {
            GlUtil.glBlendFunc(0, 768);
            GlUtil.glBlendFuncSeparate(0, 768, 1, 771);
        } else {
            GlUtil.glBlendFunc(770, 771);
            GlUtil.glBlendFuncSeparate(770, 771, 1, 771);
        }
    }

    public void update(Timer var1) {
        dataPool.checkPoolSize();
        shader.update(var1);
        this.segmentLightingUpdate.notifyUpdateQueue();
        this.getElementCollectionDrawer().update(var1, this.segmentControllers);
    }

    public void updateSegmentControllerSet() {
        synchronized(this.segmentControllers) {
            SegmentController var3;
            for(int var2 = 0; var2 < this.segmentControllers.size(); ++var2) {
                if ((Boolean)(var3 = (SegmentController)this.segmentControllers.get(var2)).getNetworkObject().markedDeleted.get() || !var3.isInClientRange()) {
                    this.segmentControllers.remove(var2);
                    --var2;
                }
            }

            this.currentCores.clear();
            Iterator var5 = this.state.getCurrentSectorEntities().values().iterator();

            while(var5.hasNext()) {
                Sendable var6;
                if ((var6 = (Sendable)var5.next()) instanceof PlanetCore) {
                    this.currentCores.add((PlanetCore)var6);
                }

                if (var6 instanceof SegmentController) {
                    var3 = (SegmentController)var6;
                    if (!this.segmentControllers.contains(var3) && var3 != null) {
                        this.segmentControllers.add(var3);
                    }
                }
            }

        }
    }

    public boolean wasSorterUpdate() {
        return this.sorterUpdate;
    }

    public void enableCulling(boolean var1) {
        this.culling = var1;
    }

    public boolean isCullFace() {
        return this.cullFace;
    }

    public void setCullFace(boolean var1) {
        this.cullFace = var1;
    }

    public ObjectArrayList<SegmentController> getSegmentControllers() {
        return this.segmentControllers;
    }

    public void checkSamples() {
        synchronized(this.drawnSegments) {
            long var2 = System.currentTimeMillis();
            synchronized(this.segmentSorter.waitForApply) {
                if (this.resorted) {
                    this.afterResort();
                    this.resorted = false;
                    long var5;
                    if ((var5 = System.currentTimeMillis() - var2) > 20L) {
                        System.err.println("RESORTING TIME : " + var5);
                    }
                }
            }

            if (this.requireFullDrawFromSort) {
                this.requireFullDrawFromSort = false;
            }

        }
    }

    public GameClientState getState() {
        return this.state;
    }

    public SegmentDrawer.SegmentRenderPass getSegmentRenderPass() {
        return this.segmentRenderPass;
    }

    public void setSegmentRenderPass(SegmentDrawer.SegmentRenderPass var1) {
        this.segmentRenderPass = var1;
    }

    public void onStopClient() {
        this.finished = true;
    }

    public ElementCollectionDrawer getElementCollectionDrawer() {
        return this.elementCollectionDrawer;
    }

    static {
        beamTmp.setIdentity();
    }

    public class TextBoxSeg {
        public final SegmentDrawer.TextBoxSeg.TextBoxElement[] v = new SegmentDrawer.TextBoxSeg.TextBoxElement[1200];
        public int pointer = 0;

        public TextBoxSeg() {
        }

        public void initialize() {
            for(int var1 = 0; var1 < this.v.length; ++var1) {
                this.v[var1] = new SegmentDrawer.TextBoxSeg.TextBoxElement();
                this.v[var1].text = new GUITextOverlay(10, 10, SegmentDrawer.this.textBoxFont, SegmentDrawer.this.getState());
                this.v[var1].text.debug = true;
                this.v[var1].text.setTextSimple("");
                this.v[var1].text.doDepthTest = true;
                this.v[var1].worldpos = new Transform();
                this.v[var1].worldpos.setIdentity();
            }

        }

        public class TextBoxElement implements Comparable<SegmentDrawer.TextBoxSeg.TextBoxElement>, Comparator<SegmentDrawer.TextBoxSeg.TextBoxElement>, TransformableSubSprite {
            public GUITextOverlay text;
            public Vector3f posBuffer = new Vector3f();
            public Transform worldpos;
            public float dist;
            public long v;
            public SegmentController c;
            public String rawText = "";
            public String realText = "";
            public Vector4f color = new Vector4f();
            public UnicodeFont font;
            public ArrayList<Replacement> replacements = new ArrayList();
            public StringBuffer buffer;

            public TextBoxElement() {
            }

            public int compareTo(SegmentDrawer.TextBoxSeg.TextBoxElement var1) {
                return Float.compare(this.dist, var1.dist);
            }

            public int compare(SegmentDrawer.TextBoxSeg.TextBoxElement var1, SegmentDrawer.TextBoxSeg.TextBoxElement var2) {
                return Float.compare(var1.dist, var2.dist);
            }

            public float getScale(long var1) {
                return -0.00395F;
            }

            public int getSubSprite(Sprite var1) {
                return 0;
            }

            public boolean canDraw() {
                return true;
            }

            public Transform getWorldTransform() {
                return this.worldpos;
            }
        }
    }

    class SegmentSorterThread extends Thread {
        protected final ObjectArrayList<SegmentDrawer.SegmentSorterThread.SegmentSortElement> presortedSegments;
        protected final ObjectArrayList<SegmentDrawer.SegmentSorterThread.Region> presortedRegions;
        private final ObjectArrayList<SegmentDrawer.SegmentSorterThread.Region> regionPool = new ObjectArrayList(1024);
        private final ObjectArrayList<SegmentDrawer.SegmentSorterThread.SegmentSortElement> segmentRegionPool = new ObjectArrayList(1024);
        private final SegmentDrawer.SegmentSorterThread.SegmentSortIterator iteratorImpl;
        private final Vector3f camPos = new Vector3f();
        private final Comparator<DrawableRemoteSegment> sortPerSegmentImpl = new Comparator<DrawableRemoteSegment>() {
            public int compare(DrawableRemoteSegment var1, DrawableRemoteSegment var2) {
                return var1.getSegmentController().getId() - var2.getSegmentController().getId();
            }
        };
        public Object waitForApply = new Object();
        public boolean applied;
        public final SegmentDrawer.TextBoxSeg textBoxes;
        private ObjectArrayList<SegmentDrawer.SAABB> saabbPool = new ObjectArrayList();
        private Int2ObjectOpenHashMap<ObjectArrayList<SegmentDrawer.SAABB>> saabbMap = new Int2ObjectOpenHashMap();
        private SegmentDrawer.SegmentSorterThread.SegmentRegionComperator segmentRegionComperator;
        private SegmentDrawer.SegmentSorterThread.RegionComparator regionComparator;
        private SegmentDrawer.SegmentSorterThread.SegmentDisposeIterator iteratorDisposeImpl;
        private HashSet<DrawableRemoteSegment> localDisposable = new HashSet();

        public SegmentSorterThread() {
            super("SegentSorter");
            this.setDaemon(true);
            this.setPriority(1);
            this.segmentRegionComperator = new SegmentDrawer.SegmentSorterThread.SegmentRegionComperator();
            this.regionComparator = new SegmentDrawer.SegmentSorterThread.RegionComparator(false);
            this.textBoxes = SegmentDrawer.this.new TextBoxSeg();
            this.presortedSegments = new ObjectArrayList();
            this.presortedRegions = new ObjectArrayList();
            this.iteratorImpl = new SegmentDrawer.SegmentSorterThread.SegmentSortIterator();
            this.iteratorDisposeImpl = new SegmentDrawer.SegmentSorterThread.SegmentDisposeIterator();
        }

        public void initialize() {
            this.textBoxes.initialize();
        }

        private void releaseAllSegmentRegion(List<SegmentDrawer.SegmentSorterThread.SegmentSortElement> var1) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
                this.releaseSegmentSortElement((SegmentDrawer.SegmentSorterThread.SegmentSortElement)var1.get(var2));
            }

        }

        private void releaseSegmentSortElement(SegmentDrawer.SegmentSorterThread.SegmentSortElement var1) {
            var1.seg = null;
            this.segmentRegionPool.add(var1);
        }

        private SegmentDrawer.SegmentSorterThread.SegmentSortElement getSegmentRegion() {
            return this.segmentRegionPool.isEmpty() ? new SegmentDrawer.SegmentSorterThread.SegmentSortElement() : (SegmentDrawer.SegmentSorterThread.SegmentSortElement)this.segmentRegionPool.remove(this.segmentRegionPool.size() - 1);
        }

        private void releaseAllRegion(List<SegmentDrawer.SegmentSorterThread.Region> var1) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
                this.releaseRegion((SegmentDrawer.SegmentSorterThread.Region)var1.get(var2));
            }

        }

        private void releaseRegion(SegmentDrawer.SegmentSorterThread.Region var1) {
            var1.buffer = null;
            this.regionPool.add(var1);
        }

        private SegmentDrawer.SegmentSorterThread.Region getRegion() {
            return this.regionPool.isEmpty() ? new SegmentDrawer.SegmentSorterThread.Region() : (SegmentDrawer.SegmentSorterThread.Region)this.regionPool.remove(this.regionPool.size() - 1);
        }

        private boolean sortRegions(ObjectArrayList<SegmentDrawer.SegmentSorterThread.Region> var1, ObjectArrayList<SegmentController> var2) {
            this.releaseAllRegion(var1);
            var1.clear();
            SimpleTransformableSendableObject var3 = SegmentDrawer.this.state.getCurrentPlayerObject();
            synchronized(var2) {
                for(int var5 = 0; var5 < var2.size(); ++var5) {
                    SegmentController var6;
                    if (!((var6 = (SegmentController)var2.get(var5)) instanceof Ship) || !((Ship)var6).isCloakedFor(var3) || ((Ship)var6).getAttachedPlayers().contains(SegmentDrawer.this.state.getPlayer())) {
                        synchronized(((SegmentBufferManager)var6.getSegmentBuffer()).getBuffer()) {
                            Iterator var8 = ((SegmentBufferManager)var6.getSegmentBuffer()).getBuffer().values().iterator();

                            while(var8.hasNext()) {
                                SegmentBufferInterface var9;
                                if (!(var9 = (SegmentBufferInterface)var8.next()).isEmpty()) {
                                    SegmentDrawer.SegmentSorterThread.Region var10;
                                    (var10 = this.getRegion()).transform.set(var6.getWorldTransformOnClient());
                                    var10.buffer = (SegmentBuffer)var9;
                                    var10.pos.set((float)((var10.buffer.getRegionStart().x + 8 << 5) - 16), (float)((var10.buffer.getRegionStart().y + 8 << 5) - 16), (float)((var10.buffer.getRegionStart().z + 8 << 5) - 16));
                                    var10.transform.transform(var10.pos);
                                    var1.add(var10);
                                }
                            }
                        }
                    }
                }
            }

            try {
                Collections.sort(var1, this.regionComparator);
                return true;
            } catch (Exception var11) {
                var11.printStackTrace();
                return false;
            }
        }

        private void sortElements(ObjectArrayList<SegmentDrawer.SegmentSorterThread.SegmentSortElement> var1, List<SegmentDrawer.SegmentSorterThread.Region> var2) throws Exception {
            this.releaseAllSegmentRegion(var1);
            var1.clear();
            SegmentDrawer.this.stats.noVisCount = 0L;
            SegmentDrawer.this.inDrawBufferCount = 0;
            int var6 = SegmentDrawer.dataPool.POOL_SIZE << 3;
            int var3 = 0;

            for(int var4 = 0; var4 < var2.size(); ++var4) {
                SegmentBuffer var5 = ((SegmentDrawer.SegmentSorterThread.Region)var2.get(var4)).buffer;
                if (var3 + var5.getTotalNonEmptySize() < var6) {
                    var3 += var5.getTotalNonEmptySize();
                    this.iteratorImpl.currentRegion = var5;
                    var5.iterateOverNonEmptyElement(this.iteratorImpl, true);
                } else if (var5.isActive()) {
                    System.err.println("DEACTIVATING REGION: " + var5.getRegionStart() + " of " + var5.getSegmentController() + "; fill: " + var3);
                    var5.iterateOverNonEmptyElement(this.iteratorDisposeImpl, true);
                }
            }

            SegmentDrawer.this.stats.lastSortTime = System.currentTimeMillis();
        }

        public void run() {
            while(!GLFrame.isFinished() && !SegmentDrawer.this.finished) {
                try {
                    this.camPos.set(Controller.getCamera().getPos());
                    this.regionComparator.pos.set(this.camPos);
                    this.iteratorImpl.camPos.set(this.camPos);
                    this.localDisposable.clear();
                    long var1 = System.currentTimeMillis();
                    if (this.sortRegions(this.presortedRegions, SegmentDrawer.this.segmentControllers)) {
                        this.sortElements(this.presortedSegments, this.presortedRegions);
                        SegmentDrawer.this.stats.timeToSort = System.currentTimeMillis() - var1;

                        try {
                            Collections.sort(this.presortedSegments, this.segmentRegionComperator);
                        } catch (Exception var12) {
                            var12.printStackTrace();
                            System.err.println("[Exception] Catched: Resorting triggered by exception");
                            continue;
                        }

                        SimpleTransformableSendableObject var19 = SegmentDrawer.this.state.getCurrentPlayerObject();
                        synchronized(SegmentDrawer.this.segmentControllers) {
                            Vector3f var3 = new Vector3f();
                            this.textBoxes.pointer = 0;
                            Iterator var4 = SegmentDrawer.this.segmentControllers.iterator();

                            label162:
                            while(true) {
                                SegmentController var5;
                                do {
                                    if (!var4.hasNext()) {
                                        break label162;
                                    }
                                } while((var5 = (SegmentController)var4.next()) instanceof Ship && ((Ship)var5).isCloakedFor(var19));

                                int var6 = var5.getTextBlocks().size();

                                for(int var7 = 0; var7 < var6; ++var7) {
                                    long var9;
                                    if (ElementCollection.getType(var9 = var5.getTextBlocks().getLong(var7)) != 670) {
                                        ElementCollection.getPosFromIndex(var9, var3);
                                        var3.x -= 16.0F;
                                        var3.y -= 16.0F;
                                        var3.z -= 16.0F;
                                        var5.getWorldTransformOnClient().transform(var3);
                                        var3.sub(this.camPos);
                                        float var8;
                                        if ((var8 = var3.length()) < 128.0F) {
                                            this.textBoxes.v[this.textBoxes.pointer].v = var9;
                                            this.textBoxes.v[this.textBoxes.pointer].dist = var8;
                                            this.textBoxes.v[this.textBoxes.pointer].c = var5;
                                            ++this.textBoxes.pointer;
                                        }

                                        if (this.textBoxes.pointer >= this.textBoxes.v.length) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        Arrays.sort(this.textBoxes.v, 0, this.textBoxes.pointer);
                        synchronized(this) {
                            assert SegmentDrawer.this.drawnSegmentsDouble.length == SegmentDrawer.dataPool.POOL_SIZE;

                            int var21 = 0;
                            int var22 = 0;

                            for(Iterator var23 = this.presortedSegments.iterator(); var23.hasNext(); ++var22) {
                                DrawableRemoteSegment var10;
                                DrawableRemoteSegment var29 = var10 = ((SegmentDrawer.SegmentSorterThread.SegmentSortElement)var23.next()).seg;
                                var29.segmentBufferAABBHelper = var29.segmentBufferAABBHelperSorting;
                                var10.segmentBufferAABBHelperSorting = null;
                                if (var22 < SegmentDrawer.dataPool.POOL_SIZE - 100) {
                                    DrawableRemoteSegment var24 = SegmentDrawer.this.drawnSegmentsDouble[var21];
                                    SegmentDrawer.this.deactivatedSegmentsDouble[var21] = var24;
                                    SegmentDrawer.this.drawnSegmentsDouble[var21] = var10;
                                    SegmentDrawer.this.drawnSegmentsDouble[var21].setSortingSerial(SegmentDrawer.this.sortingSerial);
                                    SegmentDrawer.this.drawnSegmentsDouble[var21].sortingId = var21;
                                    SegmentDrawer.this.drawnSegmentsBySegmentControllerDouble[var21] = var10;
                                    SegmentDrawer.this.drawnSegmentsBySegmentControllerDouble[var21].setSortingSerial(SegmentDrawer.this.sortingSerial);
                                    SegmentDrawer.this.drawnSegmentsBySegmentControllerDouble[var21].sortingId = var21++;
                                } else if (var10.isActive()) {
                                    this.localDisposable.add(var10);
                                    var10.setNeedsMeshUpdate(true);
                                }
                            }

                            if (var21 > 0) {
                                Arrays.sort(SegmentDrawer.this.drawnSegmentsBySegmentControllerDouble, 0, var21 - 1, this.sortPerSegmentImpl);
                            }

                            synchronized(SegmentDrawer.this.drawnSegments) {
                                SegmentDrawer.this.saabbMapLive.clear();
                                SegmentDrawer.this.saabbMapLive.putAll(this.saabbMap);

                                for(int var26 = 0; var26 < this.textBoxes.pointer; ++var26) {
                                    SegmentDrawer.this.textBoxes.v[var26].c = this.textBoxes.v[var26].c;
                                    SegmentDrawer.this.textBoxes.v[var26].dist = this.textBoxes.v[var26].dist;
                                    SegmentDrawer.this.textBoxes.v[var26].v = this.textBoxes.v[var26].v;
                                }

                                SegmentDrawer.this.textBoxes.pointer = this.textBoxes.pointer;
                                DrawableRemoteSegment[] var27 = SegmentDrawer.this.drawnSegments;
                                DrawableRemoteSegment[] var28 = SegmentDrawer.this.drawnSegmentsBySegmentController;
                                DrawableRemoteSegment[] var25 = SegmentDrawer.this.deactivatedSegments;
                                SegmentDrawer.this.drawnSegmentsPointer = var21;
                                SegmentDrawer.this.drawnSegments = SegmentDrawer.this.drawnSegmentsDouble;
                                SegmentDrawer.this.deactivatedSegments = SegmentDrawer.this.deactivatedSegmentsDouble;
                                SegmentDrawer.this.drawnSegmentsBySegmentController = SegmentDrawer.this.drawnSegmentsBySegmentControllerDouble;
                                SegmentDrawer.this.drawnSegmentsDouble = var27;
                                SegmentDrawer.this.deactivatedSegmentsDouble = var25;
                                SegmentDrawer.this.drawnSegmentsBySegmentControllerDouble = var28;
                                if (SegmentDrawer.this.drawnSegmentsPointer + 1 < SegmentDrawer.this.drawnSegments.length) {
                                    for(int var20 = SegmentDrawer.this.drawnSegmentsPointer + 1; var20 < SegmentDrawer.this.drawnSegments.length; ++var20) {
                                        SegmentDrawer.this.drawnSegments[var20] = null;
                                        SegmentDrawer.this.deactivatedSegments[var20] = null;
                                    }
                                }

                                assert SegmentDrawer.this.drawnSegmentsDouble != SegmentDrawer.this.drawnSegments : "Pointers equal...";

                                SegmentDrawer.this.requireFullDrawFromSort = true;
                            }
                        }

                        synchronized(SegmentDrawer.this.disposable) {
                            SegmentDrawer.this.disposable.addAll(this.localDisposable);
                        }

                        synchronized(this.waitForApply) {
                            SegmentDrawer.this.resorted = true;
                            this.applied = false;

                            while(!this.applied) {
                                this.waitForApply.wait();
                            }
                        }

                        Thread.sleep(500L);
                        ++SegmentDrawer.this.sortingSerial;
                    }
                } catch (InterruptedException var17) {
                    var17.printStackTrace();
                } catch (Exception var18) {
                    var18.printStackTrace();
                }
            }

        }

        class SegmentSortElement {
            float camDist;
            Vector3f pos;
            DrawableRemoteSegment seg;

            private SegmentSortElement() {
                this.pos = new Vector3f();
            }
        }

        class Region {
            private Vector3f pos;
            private Transform transform;
            private SegmentBuffer buffer;

            private Region() {
                this.pos = new Vector3f();
                this.transform = new Transform();
            }
        }

        class SegmentSortIterator implements SegmentBufferIteratorInterface {
            public SegmentBuffer currentRegion;
            Vector3f camPos;
            Vector3f tmp;

            private SegmentSortIterator() {
                this.camPos = new Vector3f();
                this.tmp = new Vector3f();
            }

            public boolean handle(Segment var1, long var2) {
                DrawableRemoteSegment var4;
                (var4 = (DrawableRemoteSegment)var1).segmentBufferAABBHelperSorting = null;
                if (!var4.isEmpty() && (var4.hasVisibleElements() || var4.occlusionFailed)) {
                    var4.segmentBufferAABBHelperSorting = this.currentRegion;

                    assert var4.getSegmentData().getSegment() == var4;

                    SegmentDrawer.SegmentSorterThread.SegmentSortElement var5;
                    (var5 = SegmentSorterThread.this.getSegmentRegion()).seg = var4;
                    var1.getSegmentController().getAbsoluteSegmentWorldPositionClient(var4, var5.pos);
                    this.tmp.sub(var5.pos, this.camPos);
                    var5.camDist = this.tmp.length();
                    var4.camDist = var5.camDist;
                    SegmentSorterThread.this.presortedSegments.add(var5);
                    ++SegmentDrawer.this.inDrawBufferCount;
                } else {
                    DebugBox var3;
                    if (var4.isEmpty()) {
                        if (EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn() && EngineSettings.P_PHYSICS_DEBUG_ACTIVE_OCCLUSION.isOn()) {
                            var3 = new DebugBox(new Vector3f((float)(var4.pos.x - 16), (float)(var4.pos.y - 16), (float)(var4.pos.z - 16)), new Vector3f((float)(var4.pos.x + 16), (float)(var4.pos.y + 16), (float)(var4.pos.z + 16)), var4.getSegmentController().getWorldTransformOnClient(), 0.0F, 0.5F, 1.0F, 1.0F);
                            DebugDrawer.boxes.add(var3);
                        }
                    } else {
                        if (!var4.hasVisibleElements() && EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn() && EngineSettings.P_PHYSICS_DEBUG_ACTIVE_OCCLUSION.isOn()) {
                            var3 = new DebugBox(new Vector3f((float)(var4.pos.x - 16), (float)(var4.pos.y - 16), (float)(var4.pos.z - 16)), new Vector3f((float)(var4.pos.x + 16), (float)(var4.pos.y + 16), (float)(var4.pos.z + 16)), var4.getSegmentController().getWorldTransformOnClient(), 1.0F, 0.5F, 0.0F, 1.0F);
                            DebugDrawer.boxes.add(var3);
                        }

                        if (var4.isActive()) {
                            SegmentSorterThread.this.localDisposable.add(var4);
                        }
                    }

                    long var10000 = SegmentDrawer.this.stats.noVisCount;
                }

                return !GLFrame.isFinished();
            }
        }

        class SegmentDisposeIterator implements SegmentBufferIteratorInterface {
            private SegmentDisposeIterator() {
            }

            public boolean handle(Segment var1, long var2) {
                DrawableRemoteSegment var4;
                (var4 = (DrawableRemoteSegment)var1).segmentBufferAABBHelperSorting = null;
                if (var4.isActive()) {
                    SegmentSorterThread.this.localDisposable.add(var4);
                }

                return !GLFrame.isFinished();
            }
        }

        class RegionComparator implements Comparator<SegmentDrawer.SegmentSorterThread.Region> {
            private Vector3f pos = new Vector3f();
            private Vector3f tV = new Vector3f();

            public RegionComparator(boolean var2) {
            }

            private float getCameraDistanceS(SegmentDrawer.SegmentSorterThread.Region var1) {
                this.tV.sub(var1.pos, this.pos);
                return this.tV.lengthSquared();
            }

            public synchronized int compare(SegmentDrawer.SegmentSorterThread.Region var1, SegmentDrawer.SegmentSorterThread.Region var2) {
                return var1.buffer == var2.buffer ? 0 : Float.compare(this.getCameraDistanceS(var1), this.getCameraDistanceS(var2));
            }
        }

        class SegmentRegionComperator implements Comparator<SegmentDrawer.SegmentSorterThread.SegmentSortElement> {
            public SegmentRegionComperator() {
            }

            public synchronized int compare(SegmentDrawer.SegmentSorterThread.SegmentSortElement var1, SegmentDrawer.SegmentSorterThread.SegmentSortElement var2) {
                return var1.seg == var2.seg ? 0 : Float.compare(var1.camDist, var2.camDist);
            }
        }
    }

    class SegmentLightingUpdateThreadManager extends Thread {
        private final ObjectArrayList<SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread> availableThreads;
        private final ObjectArrayList<SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread> allThreads;
        private final ObjectOpenHashSet<DrawableRemoteSegment> lightingUpdates = new ObjectOpenHashSet(1024);
        private final ObjectOpenHashSet<DrawableRemoteSegment> lightingUpdatesQueue = new ObjectOpenHashSet(1024);
        private final SegmentDrawer.SegComparator segComparator = SegmentDrawer.this.new SegComparator(false);
        public Object updatesPerSecond;
        public int updates;
        public long t;

        public SegmentLightingUpdateThreadManager(int var2) {
            super("SegmentLightingUpdateThreadManager");
            this.setDaemon(true);
            this.availableThreads = new ObjectArrayList(var2);
            this.allThreads = new ObjectArrayList(var2);

            for(int var4 = 0; var4 < var2; ++var4) {
                SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread var3;
                (var3 = new SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread(this)).start();
                this.availableThreads.add(var3);
                this.allThreads.add(var3);
            }

        }

        public float getAvgHandleTime() {
            float var1 = 0.0F;

            for(int var2 = 0; var2 < this.allThreads.size(); ++var2) {
                var1 += ((SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread)this.allThreads.get(var2)).getAverage();
            }

            return var1 / (float)this.allThreads.size();
        }

        public float getAvgSegLockTime() {
            float var1 = 0.0F;

            for(int var2 = 0; var2 < this.allThreads.size(); ++var2) {
                var1 += ((SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread)this.allThreads.get(var2)).getAverageLock();
            }

            return var1 / (float)this.allThreads.size();
        }

        public void addThreadToPool(SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread var1, DrawableRemoteSegment var2, boolean var3) {
            if (var3) {
                synchronized(SegmentDrawer.this.updateLocks) {
                    SegmentDrawer.this.updateLocks.remove(var2);
                }
            }

            synchronized(this.availableThreads) {
                this.availableThreads.add(var1);
                this.availableThreads.notify();
            }
        }

        public void addToUpdateQueue(DrawableRemoteSegment var1) {
            if (var1.isEmpty()) {
                var1.setNeedsMeshUpdate(false);
            } else {
                if (!var1.inLightingQueue) {
                    var1.inLightingQueue = true;
                    this.lightingUpdatesQueue.add(var1);
                }

            }
        }

        public void notifyUpdateQueue() {
            if (!this.lightingUpdatesQueue.isEmpty()) {
                synchronized(this.lightingUpdates) {
                    this.lightingUpdates.addAll(this.lightingUpdatesQueue);
                    this.lightingUpdates.notify();
                }

                this.lightingUpdatesQueue.clear();
            }

        }

        public int getQueueSize() {
            return this.lightingUpdatesQueue.size();
        }

        public void run() {
            try {
                while(Controller.getCamera() == null) {
                    Thread.sleep(100L);
                }

                label117:
                while(!GLFrame.isFinished() && !SegmentDrawer.this.finished) {
                    synchronized(this){}
                    SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread var1;
                    synchronized(this.availableThreads) {
                        while(this.availableThreads.isEmpty()) {
                            this.availableThreads.wait(10000L);
                            if (SegmentDrawer.this.finished) {
                                return;
                            }
                        }

                        var1 = (SegmentDrawer.SegmentLightingUpdateThreadManager.SegmentLightingUpdateThread)this.availableThreads.remove(0);
                    }

                    synchronized(this.lightingUpdates) {
                        while(this.lightingUpdates.isEmpty()) {
                            this.lightingUpdates.wait(1000L);
                            if (SegmentDrawer.this.finished) {
                                return;
                            }
                        }

                        this.segComparator.pos.set(Controller.getCamera().getPos());
                        DrawableRemoteSegment var3 = null;
                        Iterator var4 = this.lightingUpdates.iterator();

                        while(true) {
                            DrawableRemoteSegment var5;
                            do {
                                if (!var4.hasNext()) {
                                    DrawableRemoteSegment var12 = var3;
                                    this.lightingUpdates.remove(var3);
                                    var3.inLightingQueue = false;
                                    synchronized(SegmentDrawer.this.updateLocks) {
                                        synchronized(SegmentDrawer.this.disposable) {
                                            if (var12.isInUpdate() || SegmentDrawer.this.updateLocks.contains(var12)) {
                                                this.addThreadToPool(var1, var12, false);
                                                continue label117;
                                            }

                                            SegmentDrawer.this.updateLocks.add(var12);
                                        }
                                    }

                                    synchronized(var3.cubeMeshLock) {
                                        var12.setInUpdate(true);
                                        var12.lastLightingUpdateTime = System.currentTimeMillis();
                                        var12.setNeedsMeshUpdate(false);
                                    }

                                    var1.addToUpdate(var3);
                                    continue label117;
                                }

                                var5 = (DrawableRemoteSegment)var4.next();
                            } while(var3 != null && this.segComparator.compare(var5, var3) >= 0);

                            var3 = var5;
                        }
                    }
                }

            } catch (InterruptedException var11) {
                var11.printStackTrace();
            }
        }

        class SegmentLightingUpdateThread extends Thread {
            DrawableRemoteSegment nextSeg;
            DrawableRemoteSegment updatingSeg;
            private Occlusion occlusionData;
            private SegmentDrawer.SegmentLightingUpdateThreadManager manager;
            private double takenLock;
            private double taken;
            private int handled = 1;
            private int id;

            public SegmentLightingUpdateThread(SegmentDrawer.SegmentLightingUpdateThreadManager var2) {
                super("LightUpdate" + SegmentDrawer.threadCount);
                this.id = SegmentDrawer.threadCount++;
                this.setPriority(4);
                this.initializeOcclusion();
                this.manager = var2;
                this.setDaemon(true);
            }

            public float getAverage() {
                return (float)(this.taken / (double)this.handled);
            }

            public float getAverageLock() {
                return (float)(this.takenLock / (double)this.handled);
            }

            public void addToUpdate(DrawableRemoteSegment var1) {
                synchronized(this) {
                    this.nextSeg = var1;
                    this.notify();
                }
            }

            private void resetOcclusion(SegmentData var1, CubeMeshBufferContainer var2) {
                if (var1.getSize() > 0) {
                    assert var1 != null;

                    this.occlusionData.reset(var1, var2);
                }
            }

            private void compute(DrawableRemoteSegment var1, CubeMeshBufferContainer var2, CubeData var3, int var4) {
                try {
                    if (!var1.isEmpty() && var1.getSegmentData() != null) {
                        SegmentData var11 = var1.getSegmentData();
                        long var5 = System.currentTimeMillis();
                        var11.rwl.readLock().lock();
                        this.takenLock += (double)(System.currentTimeMillis() - var5);

                        try {
                            this.resetOcclusion(var11, var2);
                            this.occlusionData.compute(var11, var2);
                            if (!var1.occlusionFailed || var1.getCurrentCubeMesh() == null) {
                                assert var3 != null;

                                var3.createIndex(var11, var2);
                            }
                        } finally {
                            var11.rwl.readLock().unlock();
                        }

                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                    System.err.println("[CLIENT] Exception: " + var10.getClass().getSimpleName() + " in computing Lighting of " + var1.getSegmentController() + ". retrying");
                    var1.setHasVisibleElements(true);
                    var1.occlusionFailed = true;
                    var1.occlusionFailTime = System.currentTimeMillis();
                }
            }

            private void handle(DrawableRemoteSegment var1) {
                assert var1.getNextCubeMesh() == null;

                synchronized(var1.cubeMeshLock) {
                    CubeData var3 = SegmentDrawer.dataPool.getMesh(var1);
                    var1.setNextCubeMesh(var3);
                    CubeMeshBufferContainer var4 = var1.getContainerFromPool();
                    this.compute(var1, var4, var3, 0);
                    synchronized(SegmentDrawer.this.generatedSegments) {
                        int var9;
                        if ((var9 = SegmentDrawer.this.generatedSegments.indexOf(var1)) >= 0) {
                            DrawableRemoteSegment var10 = (DrawableRemoteSegment)SegmentDrawer.this.generatedSegments.remove(var9);

                            assert var1 != var10;

                            synchronized(SegmentDrawer.this.disposable) {
                                SegmentDrawer.this.disposable.add(var10);
                            }

                            var10.setInUpdate(false);
                        }

                        SegmentDrawer.this.generatedSegments.add(var1);
                    }
                }

                ++this.manager.updates;
            }

            public void initializeOcclusion() {
                this.occlusionData = new Occlusion(this.id);
            }

            public void run() {
                try {
                    while(!GLFrame.isFinished() && !SegmentDrawer.this.finished) {
                        synchronized(this) {
                            while(this.nextSeg == null && this.updatingSeg == null) {
                                this.wait(10000L);
                                if (SegmentDrawer.this.finished) {
                                    return;
                                }
                            }

                            this.updatingSeg = this.nextSeg;
                            this.nextSeg = null;
                        }

                        long var1 = System.currentTimeMillis();
                        this.handle(this.updatingSeg);
                        this.taken += (double)(System.currentTimeMillis() - var1);
                        ++this.handled;
                        Thread.sleep(2L);
                        this.manager.addThreadToPool(this, this.updatingSeg, true);
                        this.updatingSeg = null;
                    }

                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }
        }
    }

    class SegDrawStats {
        public long noVisCount;
        public long lastLightQueueInfoUpdate;
        private long timeForDrawVisible;
        private long timeForUniforms;
        private long timeForActualDraw;
        private long timeForTotalDraw;
        private long drawnBoxes;
        private long meshUpdateTime;
        private long timeForFrustum;
        private long timeForCheckUpdate;
        private long timeToSort;
        private long lastSortTime;

        private SegDrawStats() {
        }
    }

    class SegComparator implements Comparator<DrawableRemoteSegment> {
        private final Vector3f pos = new Vector3f();
        private Vector3f tV = new Vector3f();

        public SegComparator(boolean var2) {
        }

        public int compare(DrawableRemoteSegment var1, DrawableRemoteSegment var2) {
            return var1 != var2 && !var1.equals(var2) ? Float.compare(var1.camDist, var2.camDist) : 0;
        }
    }

    public class SAABB {
        public int pointer;
        Vector3i position = new Vector3i();
        Vector3f min = new Vector3f();
        Vector3f max = new Vector3f();

        public SAABB() {
        }

        public void reset() {
            this.min.set(2.14748365E9F, 2.14748365E9F, 2.14748365E9F);
            this.max.set(-2.14748365E9F, -2.14748365E9F, -2.14748365E9F);
        }
    }

    public static enum SegmentRenderPass {
        OPAQUE,
        TRANSPARENT,
        ALL;

        private SegmentRenderPass() {
        }
    }
}
