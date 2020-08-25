//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
//all hail the ironsight
package org.schema.game.server.data;

import api.listener.events.UniverseGenerationEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import obfuscated.v;
import obfuscated.v.g;
import org.lwjgl.opengl.GL11;
import org.namegen.NameGenerator;
import org.schema.common.FastMath;
import org.schema.common.util.ByteUtil;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.gamemap.PositionableSubSpriteCollection;
import org.schema.game.client.view.gamemap.PositionableSubSpriteCollectionReal;
import org.schema.game.client.view.gamemap.StarPosition;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.common.data.world.SectorInformation.SectorType;
import org.schema.game.server.data.simulation.npc.NPCFactionManager;
import org.schema.game.server.data.simulation.npc.geo.FactionResourceRequestContainer;
import org.schema.schine.graphicsengine.camera.Camera;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Sprite;

public class Galaxy {
    public static final int TYPE_SUN = 0;
    public static final int TYPE_GIANT = 1;
    public static final int TYPE_BLACK_HOLE = 2;
    public static final int TYPE_DOUBLE_STAR = 3;
    public static int size = 128;
    public static int sizeSquared = 16384;
    public static int halfSize;
    public static boolean USE_GALAXY;
    public final Vector3i galaxyPos;
    private final IntArrayList starIndices = new IntArrayList();
    private final Vector2f ttmp = new Vector2f();
    private final Vector3f normalStd = new Vector3f(0.0F, 0.0F, 1.0F);
    public Long2ObjectOpenHashMap<String> nameCache = new Long2ObjectOpenHashMap();
    Random random;
    Random randomNPC;
    Vector3i tmpCPOS = new Vector3i();
    Vector3i tmpout = new Vector3i();
    Vector3i tmpoutFrom = new Vector3i();
    Vector3i tmpoutTo = new Vector3i();
    Vector3f tmpAxis = new Vector3f();
    Vector3f tmpRight = new Vector3f();
    Vector3f tmpFwd = new Vector3f();
    private long seed;
    private int displayList;
    private byte[][][] galaxyMap;
    private Vector4f[] colors;
    private float[] colorIndices;
    private int[] types;
    private float[] intensity;
    private int numberOfStars;
    private byte threshHold;
    private PositionableSubSpriteCollectionReal spriteCollection;
    private final List<Vector3i> blackHoles;
    private NameGenerator nameGenerator;
    private final Galaxy.CComp sorter;
    private final Vector3i tmp;
    private NPCFactionManager npcFactionManager;
    private v[] resourceNoises;

    public Galaxy(long var1, Vector3i var3) {
        this.galaxyMap = new byte[size][size][size];
        this.colors = new Vector4f[2048];
        this.colorIndices = new float[2048];
        this.types = new int[2048];
        this.intensity = new float[2048];
        this.threshHold = 10;
        this.blackHoles = new ObjectArrayList();
        this.sorter = new Galaxy.CComp();
        this.tmp = new Vector3i();
        this.resourceNoises = new v[16];
        this.setSeed(var1);
        this.random = new Random(var1);
        this.randomNPC = new Random(var1);
        this.galaxyPos = var3;

        try {
            this.nameGenerator = new NameGenerator("./data/config/systemNames.syl", var1);
        } catch (IOException var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4);
        }

        Random var5 = new Random(var1);

        for(int var2 = 0; var2 < this.resourceNoises.length; ++var2) {
            this.resourceNoises[var2] = new v(var5.nextInt());
            this.resourceNoises[var2].a(g.c);
            this.resourceNoises[var2].a(0.22F);
        }

    }

    public static int localCoordinate(int var0) {
        return ByteUtil.modU128(var0);
    }

    public void initializeGalaxyOnServer(GameServerState var1) {
        this.generateBlackHoleNetworkOnServer(var1);
        this.npcFactionManager = new NPCFactionManager(var1, this);
    }

    public static Vector3i getContainingGalaxyFromSystemPos(Vector3i var0, Vector3i var1) {
        var1.x = var0.x + halfSize >= 0 ? (var0.x + halfSize) / size : (var0.x + halfSize) / size - 1;
        var1.y = var0.y + halfSize >= 0 ? (var0.y + halfSize) / size : (var0.y + halfSize) / size - 1;
        var1.z = var0.z + halfSize >= 0 ? (var0.z + halfSize) / size : (var0.z + halfSize) / size - 1;
        return var1;
    }

    public static void main(String[] var0) {
        for(int var1 = -200; var1 < 200; ++var1) {
            System.err.println("TT " + var1 + " -> " + localCoordinate(var1));
        }

    }

    public static Vector2f randomDirection2D(Random var0, Vector2f var1) {
        float var2 = var0.nextFloat() * 2.0F * 3.1415927F;
        var1.set(FastMath.cos(var2), FastMath.sin(var2));
        return var1;
    }

    public static Vector3f randomDirection3D(Random var0, Vector3f var1, Vector2f var2) {
        float var3 = 2.0F * var0.nextFloat() - 1.0F;
        Vector2f var4;
        (var4 = randomDirection2D(var0, var2)).scale(FastMath.carmackSqrt(1.0F - var3 * var3));
        var1.set(var4.x, var4.y, var3);
        return var1;
    }

    public static double normalize(double var0, double var2, double var4, double var6, double var8) {
        return (var0 - var2) / (var4 - var2) * (var8 - var6) + var6;
    }

    public static double distance2D(double var0, double var2, double var4, double var6) {
        double var8 = var0 - var4;
        double var10 = var2 - var6;
        return Math.sqrt(var8 * var8 + var10 * var10);
    }

    public static double rnd(double var0, Random var2) {
        return var0 * var2.nextDouble();
    }

    public static double rnd(double var0, double var2, Random var4) {
        return var0 + (var2 - var0) * var4.nextDouble();
    }

    public static Vector3i getLocalCoordinatesFromSystem(Vector3i var0, Vector3i var1) {
        int var2 = localCoordinate(var0.x + halfSize);
        int var3 = localCoordinate(var0.y + halfSize);
        int var4 = localCoordinate(var0.z + halfSize);
        var1.set(var2, var3, var4);
        return var1;
    }

    public static Vector3i getRelPosInGalaxyFromAbsSystem(Vector3i var0, Vector3i var1) {
        var1.set(var0);
        var1.add(halfSize, halfSize, halfSize);
        var1.x = localCoordinate(var1.x);
        var1.y = localCoordinate(var1.y);
        var1.z = localCoordinate(var1.z);
        return var1;
    }

    public static boolean isPlanetOrbit(int var0) {
        return var0 > 1073741823;
    }

    public void generate() {
        this.random.setSeed(this.getSeed());
        this.randomNPC.setSeed(this.getSeed());
        this.generate((double)(165000 + this.random.nextInt(10000) - 5000), (double)(4 + this.random.nextInt(2) - (this.random.nextInt(10) == 0 ? 1 : 0)), (double)(12 + this.random.nextInt(5) - 2), (double)halfSize, 0.15D + (double)(this.random.nextFloat() * 0.05F - 0.025F));
    }

    public void generate(double var1, double var3, double var5, double var7, double var9) {
        //var1 starCount, var3 armCount, var5 spread, var7 range, var9 rotation)
        int starChance = 10;
      //  public static final int TYPE_SUN = 0;
      //  public static final int TYPE_GIANT = 1;
      //  public static final int TYPE_BLACK_HOLE = 2;
      //  public static final int TYPE_DOUBLE_STAR = 3;

        UniverseGenerationEvent universeGenerationEvent = new UniverseGenerationEvent(var1,var3,var5,var7,var9, starChance);
        StarLoader.fireEvent(UniverseGenerationEvent.class, universeGenerationEvent, true);

        var1 = universeGenerationEvent.getStarCount();
        var3 = universeGenerationEvent.getArmCount();
        var5 = universeGenerationEvent.getSpread();
        var7 = universeGenerationEvent.getRange();
        var9 = universeGenerationEvent.getRange();
        starChance = universeGenerationEvent.getStarChance();

        long var11 = System.currentTimeMillis();
        //create x y grid?
        int var13;
        int var14;
        for(var13 = 0; var13 < size; ++var13) {
            for(var14 = 0; var14 < size; ++var14) {
                Arrays.fill(this.galaxyMap[var13][var14], (byte)0);
            }
        }

        this.random.setSeed(this.getSeed());
        this.randomNPC.setSeed(this.getSeed());

        for(var13 = 0; var13 < this.types.length; ++var13) {
            if (0.8500000238418579D + this.random.nextGaussian() * 0.20000000298023224D > 0.5D) {
                this.types[var13] = 0;
            } else if ((double)this.random.nextFloat() > 0.5D) {
                this.types[var13] = 1;
            } else if (this.random.nextBoolean()) {
                this.types[var13] = 2;
            } else {
                this.types[var13] = 3;
            }
        }
//create a bunch of semi random colours to use for the stars?
        for(var13 = 0; var13 < this.colors.length; ++var13) {
            if (0.8500000238418579D + this.random.nextGaussian() * 0.20000000298023224D > 0.65D) {
                this.colors[var13] = new Vector4f(1.0F, 1.0F, 1.0F, (float)Math.min(1.0D, 0.8500000238418579D + this.random.nextGaussian() * 0.20000000298023224D));
                this.colorIndices[var13] = 0.0625F;
                this.intensity[var13] = 1.0F;
            } else {
                float var18;
                if ((double)this.random.nextFloat() > 0.5D) {
                    var18 = this.random.nextFloat() * 0.9F + 0.1F;
                    this.colors[var13] = new Vector4f(1.0F, 1.0F, 0.9F * var18, (float)Math.min(1.0D, 0.8500000238418579D + this.random.nextGaussian() * 0.20000000298023224D));
                    this.colorIndices[var13] = 0.3125F;
                    this.intensity[var13] = 0.8F;
                } else if (this.random.nextBoolean()) {
                    var18 = this.random.nextFloat() * 0.9F + 0.1F;
                    this.colors[var13] = new Vector4f(0.9F * var18, 0.9F * var18, 1.0F, (float)Math.min(1.0D, 0.8500000238418579D + this.random.nextGaussian() * 0.20000000298023224D));
                    this.colorIndices[var13] = 0.5F;
                    this.intensity[var13] = 2.0F;
                } else {
                    var18 = this.random.nextFloat() * 0.9F + 0.1F;
                    this.colors[var13] = new Vector4f(1.0F, 0.7F * var18, 0.7F * var18, (float)Math.min(1.0D, 0.8500000238418579D + this.random.nextGaussian() * 0.20000000298023224D));
                    this.colorIndices[var13] = 0.9375F;
                    this.intensity[var13] = 0.5F;
                }
            }
        }

        this.random.setSeed(this.getSeed());
        this.starIndices.clear();
        this.numberOfStars = 0;
//star creation
        //var13 probably was i at one point, its the same across all forloops but gets reset to zero everytime -> probably not a meaningful var to store stuff
        for(var13 = 0; (double)var13 < var1; ++var13) { //whats var 1 ?
            //iterator is not each star.
            //skipping iterators here results in smaller radius universes

            double var28 = (double)((int)Math.floor((double)var13 / (var1 / var3))) * (360.0D / var3); //360 degree -> is this the spiral maker?
            double var16;
            if (this.random.nextDouble() > 0.5D) {
                var16 = rnd(0.1D, 1.0D, this.random);
            } else {
                var16 = rnd(1.0D, 2.0D, this.random);
            }

            double var32 = rnd(0.0D, var7, this.random) * rnd(1.0D, rnd(rnd(var16, this.random), this.random), this.random);
            double var20 = rnd(0.0D, rnd(var5, this.random), this.random);
            if (this.random.nextDouble() > 0.5D) {
                var20 = -var20;
            }

            double var22 = var32 * Math.cos(var28 + var32 * var9) + rnd(rnd(rnd(-var5, this.random), this.random), rnd(rnd(var5, this.random), this.random), this.random);
            double var24 = var32 * Math.sin(var28 + var32 * var9) + rnd(rnd(rnd(-var5, this.random), this.random), rnd(rnd(var5, this.random), this.random), this.random);
            double var26 = Math.cos(Math.min(90.0D, normalize(distance2D(var22, var24, 0.0D, 0.0D), 0.0D, var7 / 2.0D, 0.0D, 180.0D) / 2.0D)) * rnd(rnd(-var5, this.random), rnd(var5, this.random), this.random) / 2.0D + var20 / 10.0D;
            var14 = (int)Math.max(0.0D, Math.min((double)(size - 1), var22 + (double)halfSize));
            int var15 = (int)Math.max(0.0D, Math.min((double)(size - 1), var26 + (double)halfSize));
            int var30 = (int)Math.max(0.0D, Math.min((double)(size - 1), var24 + (double)halfSize));
            byte var17 = this.galaxyMap[var30][var15][var14];
            this.galaxyMap[var30][var15][var14] = (byte)Math.min(127, var17 + 1);
            if (this.galaxyMap[var30][var15][var14] == this.threshHold + 1) { //threshold is 10, weighted system. value > treshhold +1 => star creatin
                /* THUS BEGINS ITHIR'S CHANGES */
                if(random.nextInt(starChance) != 0){
                    this.galaxyMap[var30][var15][var14] = 0; //sets weight value to zero, -> no star creation
                    //Nulled the star
                }
                /* THUS ENDS ITHIR'S CHANGES */
                else {
                    //Didn't null the star

                    ++this.numberOfStars;
                    int var31 = this.getIndex(var14, var15, var30); //has structure of ax^2 + bx + c - why? is this an ID describing the pos?

                    assert var31 >= 0;

                    if (this.getSystemType(var14, var15, var30) == 2) {
                        this.blackHoles.add(new Vector3i(var14 - halfSize, var15 - halfSize, var30 - halfSize));
                    } else {
                        //adds ID(?) previously generated to list
                        this.starIndices.add(var31);
                    }
                }
            }
        }

        if (this.blackHoles.size() > 0) {
            ((ObjectArrayList)this.blackHoles).trim();
        }

        if (this.starIndices.size() > 0) {
            this.starIndices.trim();
        }

        long var29 = System.currentTimeMillis() - var11;
        System.err.println("[UNIVERSE] creation time: " + var29 + " ms");
    }

    public String getBlockHoleUID(Vector3i var1, Vector3i var2) {
        Vector3i var3;
        (var3 = new Vector3i(var1)).add(halfSize, halfSize, halfSize);
        var3 = this.getSunPositionOffset(var3, this.tmpCPOS);
        var2.set(8, 8, 8);
        var2.add(var3);
        var2.add(var1.x << 4, var1.y << 4, var1.z << 4);
        var2.add(this.galaxyPos.x * size << 4, this.galaxyPos.y * size << 4, this.galaxyPos.z * size << 4);
        return "BH_" + var2.x + "_" + var2.y + "_" + var2.z + "_OO_" + var3.x + "_" + var3.y + "_" + var3.z;
    }

    public void generateBlackHoleNetworkOnServer(GameServerState var1) {
        if (USE_GALAXY) {
            if (this.blackHoles.size() > 1) {
                Vector3i var2 = (Vector3i)this.blackHoles.get(0);
                String var3 = this.getBlockHoleUID(var2, this.tmpout);
                if (var1.getDatabaseIndex().getTableManager().getFTLTable().getFtl(this.tmpout, var3) == null) {
                    System.err.println("[SERVER][GALAXY] Generating Black Hole FTL");
                    Collections.sort(this.blackHoles, new Comparator<Vector3i>() {
                        public int compare(Vector3i var1, Vector3i var2) {
                            return Float.compare(var1.lengthSquared(), var2.lengthSquared());
                        }
                    });
                    int var9 = 0;
                    int var4 = this.blackHoles.size();
                    if (this.blackHoles.size() > 0) {
                        try {
                            for(; this.blackHoles.size() > 0; var1.getUniverse().ftlDirty.enqueue(new Vector3i(this.tmpoutFrom))) {
                                Vector3i var5 = (Vector3i)this.blackHoles.remove(0);
                                Vector3i var6 = null;
                                if (this.blackHoles.size() > 0) {
                                    for(int var7 = 0; var7 < this.blackHoles.size(); ++var7) {
                                        if (var6 == null) {
                                            var6 = (Vector3i)this.blackHoles.get(var7);
                                        } else if (Vector3fTools.lengthSquared(var5, (Vector3i)this.blackHoles.get(var7)) < Vector3fTools.lengthSquared(var5, var6)) {
                                            var6 = (Vector3i)this.blackHoles.get(var7);
                                        }
                                    }
                                } else {
                                    var6 = var2;
                                }

                                assert !var5.equals(var6);

                                String var11 = this.getBlockHoleUID(var5, this.tmpoutFrom);
                                String var10 = this.getBlockHoleUID(var6, this.tmpoutTo);

                                assert this.getSystemType(getLocalCoordinatesFromSystem(VoidSystem.getContainingSystem(this.tmpoutFrom, new Vector3i()), new Vector3i())) == 2;

                                assert this.getSystemType(getLocalCoordinatesFromSystem(VoidSystem.getContainingSystem(this.tmpoutTo, new Vector3i()), new Vector3i())) == 2;

                                if (var1.getDatabaseIndex().getTableManager().getFTLTable().insertFTLEntry(var11, this.tmpoutFrom, new Vector3i(), var10, this.tmpoutTo, new Vector3i(), 1, 0)) {
                                    ++var9;
                                } else {
                                    assert false;
                                }
                            }
                        } catch (SQLException var8) {
                            var8.printStackTrace();
                        }

                        assert var9 == var4 : var9 + "; " + var4;
                    }
                }
            }

        }
    }

    public int getIndex(int var1, int var2, int var3) {
        return var3 * sizeSquared + var2 * size + var1;
    }

    public void getNormalizedPosFromIndex(int var1, Vector3f var2) {
        int var3 = var1 / sizeSquared;
        int var4 = (var1 -= var3 * sizeSquared) / size;
        var1 -= var4 * size;
        var2.set((float)(var1 - halfSize), (float)(var4 - halfSize), (float)(var3 - halfSize));
    }

    public void getPosFromIndex(int var1, Vector3f var2) {
        int var3 = var1 / sizeSquared;
        int var4 = (var1 -= var3 * sizeSquared) / size;
        var1 -= var4 * size;
        var2.set((float)var1, (float)var4, (float)var3);
    }

    public void getNormalizedPosFromIndex(int var1, Vector3i var2) {
        int var3 = var1 / sizeSquared;
        int var4 = (var1 -= var3 * sizeSquared) / size;
        var1 -= var4 * size;
        var2.set(var1 - halfSize, var4 - halfSize, var3 - halfSize);
    }

    public void getPosFromIndex(int var1, Vector3i var2) {
        int var3 = var1 / sizeSquared;
        int var4 = (var1 -= var3 * sizeSquared) / size;
        var1 -= var4 * size;
        var2.set(var1, var4, var3);
    }

    public byte getHeatFromIndex(int var1) {
        int var2 = var1 / sizeSquared;
        int var3 = (var1 -= var2 * sizeSquared) / size;
        var1 -= var3 * size;
        return this.galaxyMap[var2][var3][var1];
    }

    public void updateLocal(long var1) {
        this.npcFactionManager.updateLocal(var1);
    }

    public void clean() {
        if (this.displayList != 0) {
            GL11.glDeleteLists(this.displayList, 1);
        }

    }

    public void onInit() {
        this.spriteCollection = this.getPositionSpritesReal();
    }

    public int getRandomPlusMinus(Random var1) {
        if (var1.nextInt(15) > 0) {
            return 0;
        } else if (var1.nextInt(15) > 0) {
            return var1.nextBoolean() ? 2 : -2;
        } else {
            return var1.nextBoolean() ? 1 : -1;
        }
    }

    public int[] getSystemOrbits(Vector3i var1, int[] var2) {
        this.random.setSeed(this.getSystemSeed(var1));

        for(int var3 = 0; var3 < 8; ++var3) {
            var2[var3] = this.random.nextFloat() > 0.45F ? this.random.nextInt(2147483646) + 1 : 0;
        }

        return var2;
    }

    public long getSystemSeed(Vector3i var1) {
        return (long)var1.hashCode() * this.seed;
    }

    public long getSystemSeed(int var1, int var2, int var3) {
        return (long)(((var1 ^ var1 >>> 16) * 15 + (var2 ^ var2 >>> 16)) * 15 + (var3 ^ var3 >>> 16)) * this.seed;
    }

    public Vector3f getSystemAxis(Vector3i var1, Vector3f var2) {
        this.random.setSeed(this.getSystemSeed(var1));
        randomDirection3D(this.random, var2, this.ttmp);
        var2.normalize();
        return var2;
    }

    public Vector4f getSunColor(int var1, int var2, int var3) {
        return this.colors[Math.abs(var1 * 123 + var2 * var1 * var3 * (var1 + 132) + var2 + var3) % this.colors.length];
    }

    public float getSunColorIndex(int var1, int var2, int var3) {
        return this.colorIndices[Math.abs(var1 * 123 + var2 * var1 * var3 * (var1 + 132) + var2 + var3) % this.colorIndices.length];
    }

    public Vector4f getSunColor(Vector3i var1) {
        return this.getSunColor(var1.x, var1.y, var1.z);
    }

    public int getSystemType(int var1, int var2, int var3) { //add semi random to get less stars here? TODO star creation
        return (FastMath.abs((float)(var1 - 64)) >= 2.0F || FastMath.abs((float)(var2 - 64)) >= 2.0F || FastMath.abs((float)(var3 - 64)) >= 2.0F) && (var1 != 130000000 || var2 != 130000000 || var3 != 130000000) ? this.types[Math.abs(var1 * 123 + var2 * var1 * var3 * (var1 + 132) + var2 + var3) % this.types.length] : 0;
    }

    public int getSystemType(Vector3i var1) {
        return this.getSystemType(var1.x, var1.y, var1.z);
    }

    public float getSystemSunIntensity(int var1, int var2, int var3) {
        return this.intensity[Math.abs(var1 * 123 + var2 * var1 * var3 * (var1 + 132) + var2 + var3) % this.types.length];
    }

    public float getSystemSunIntensity(Vector3i var1) {
        return this.getSystemSunIntensity(var1.x, var1.y, var1.z);
    }

    public Vector3i getSunPositionOffset(Vector3i var1, Vector3i var2) {
        if (!USE_GALAXY) {
            var2.set(0, 0, 0);
            return var2;
        } else {
            this.random.setSeed(this.getSystemSeed(var1));
            var2.set(this.getRandomPlusMinus(this.random), this.getRandomPlusMinus(this.random), this.getRandomPlusMinus(this.random));
            return var2;
        }
    }

    public void getAxisMatrix(Vector3i var1, Matrix3f var2) {
        this.getSystemAxis(var1, this.tmpAxis);
        this.tmpRight.cross(this.tmpAxis, this.normalStd);
        this.tmpRight.normalize();
        this.tmpFwd.cross(this.tmpRight, this.tmpAxis);
        this.tmpFwd.normalize();
        GlUtil.setUpVector(this.tmpAxis, var2);
        GlUtil.setForwardVector(this.tmpFwd, var2);
        GlUtil.setRightVector(this.tmpRight, var2);

        assert var2.determinant() != 0.0F : var2;

    }

    public void getPositions(ObjectArrayList<Vector3f> var1, FloatArrayList var2) {
        for(int var3 = 0; var3 < size; ++var3) {
            for(int var4 = 0; var4 < size; ++var4) {
                for(int var5 = 0; var5 < size; ++var5) {
                    if (this.galaxyMap[var3][var4][var5] > this.threshHold) {
                        Vector3f var6 = new Vector3f((float)(this.galaxyPos.x * size + var5), (float)(this.galaxyPos.y * size + var4), (float)(this.galaxyPos.z * size + var3));
                        this.getSystemType(var5, var4, var3);
                        var1.add(var6);
                        var2.add(this.getSunColorIndex(var5, var4, var3));
                    }
                }
            }
        }

    }

    public PositionableSubSpriteCollectionReal getPositionSpritesReal() {
        PositionableSubSpriteCollectionReal var1 = new PositionableSubSpriteCollectionReal();
        this.getNumberOfStars();
        int var2 = 0;
        Vector3i var3 = new Vector3i();
        Vector3i var4 = new Vector3i();
        Vector3i var5 = new Vector3i();

        for(int var6 = 0; var6 < size; ++var6) {
            for(int var7 = 0; var7 < size; ++var7) {
                for(int var8 = 0; var8 < size; ++var8) {
                    if (this.galaxyMap[var6][var7][var8] > this.threshHold) {
                        var4.set(var8, var7, var6);
                        StarPosition var9 = new StarPosition();
                        int var10 = this.getSystemType(var8, var7, var6);
                        this.getSunPositionOffset(var4, var3);
                        var9.relPosInGalaxy.set(var8, var7, var6);
                        var9.pos.set((float)(this.galaxyPos.x * size) + (float)var8 + (float)var3.x * 0.0625F - (float)halfSize, (float)(this.galaxyPos.y * size) + (float)var7 + (float)var3.y * 0.0625F - (float)halfSize, (float)(this.galaxyPos.z * size) + (float)var6 + (float)var3.z * 0.0625F - (float)halfSize);
                        var9.color.set(this.getSunColor(var8, var7, var6));
                        switch(var10) {
                            case 0:
                                var9.starSubSprite = 0;
                                break;
                            case 1:
                                var9.starSubSprite = 1;
                                break;
                            case 2:
                                var9.starSubSprite = 2;
                                break;
                            case 3:
                                StarPosition var11 = new StarPosition();
                                var4.set(0, 0, 0);
                                VoidSystem.getSecond(var4, var3, var5);
                                var11.pos.set((float)(this.galaxyPos.x * size) + (float)var8 + (float)var5.x * 0.0625F - (float)halfSize, (float)(this.galaxyPos.y * size) + (float)var7 + (float)var5.y * 0.0625F - (float)halfSize, (float)(this.galaxyPos.z * size) + (float)var6 + (float)var5.z * 0.0625F - (float)halfSize);
                                var11.color.set(this.getSunColor(var8 + 30, var7 + 30, var6 + 30));
                                var11.starSubSprite = 0;
                                var1.add(var11);
                        }

                        var1.add(var9);
                        ++var2;
                    }
                }
            }
        }

        assert var2 == this.getNumberOfStars();

        return var1;
    }

    public boolean isStellarSystem(Vector3i var1) {
        return this.galaxyMap[var1.z][var1.y][var1.x] > this.threshHold && (this.getSystemType(var1) == 3 || this.getSystemType(var1) == 1 || this.getSystemType(var1) == 0);
    }

    public PositionableSubSpriteCollection getPositionSprites() {
        float[] var1 = new float[this.getNumberOfStars() << 3];
        int var2 = 0;

        for(int var3 = 0; var3 < size; ++var3) {
            for(int var4 = 0; var4 < size; ++var4) {
                for(int var5 = 0; var5 < size; ++var5) {
                    byte var10000 = this.galaxyMap[var3][var4][var5];
                    boolean var6 = false;
                    if (var10000 > this.threshHold) {
                        int var7 = var2 << 3;
                        var1[var7] = (float)(var5 - halfSize);
                        var1[var7 + 1] = (float)(var4 - halfSize);
                        var1[var7 + 2] = (float)(var3 - halfSize);
                        var1[var7 + 3] = 1.0F;
                        var1[var7 + 4] = 1.0F;
                        var1[var7 + 5] = 1.0F;
                        var1[var7 + 6] = 1.0F;
                        var1[var7 + 7] = 0.0F;
                        ++var2;
                    }
                }
            }
        }

        assert var2 == this.getNumberOfStars();

        return new PositionableSubSpriteCollection(var1);
    }

    public void draw(Camera var1, int var2, float var3) {
        GlUtil.glBindTexture(3553, 0);
        GlUtil.glEnable(3553);
        GlUtil.glEnable(3042);
        GlUtil.glBlendFunc(1, 1);
        Sprite var4;
        (var4 = Controller.getResLoader().getSprite("stellarSprites-2x2-c-")).setBillboard(true);
        var4.setBlend(true);
        var4.setFlip(true);
        var1.updateFrustum();
        var4.blendFunc = 0;
        var4.setDepthTest(true);
        if (this.spriteCollection == null) {
            this.onInit();
        }

        assert this.spriteCollection != null;

        StarPosition.posMult = var2;
        StarPosition.posAdd = 3.125F;
        StarPosition.spriteScale = var3;
        Vector3f var7 = new Vector3f(var1.getPos());
        if (this.sorter.pos == null || !this.sorter.pos.equals(var7)) {
            this.sorter.pos.set(var7);
            int var8 = this.spriteCollection.size();

            for(int var5 = 0; var5 < var8; ++var5) {
                StarPosition var6;
                (var6 = (StarPosition)this.spriteCollection.get(var5)).setDistanceToCam(Vector3fTools.length(var7, var6.getPos()));
            }

            Collections.sort(this.spriteCollection, this.sorter);
        }

        var4.setSelectionAreaLength(24.0F);
        Sprite.draw3D(var4, this.spriteCollection, var1);
        var4.setSelectionAreaLength(0.0F);
        var4.blendFunc = 0;
        StarPosition.posMult = 1;
        StarPosition.posAdd = 0.0F;
        StarPosition.spriteScale = 1.0F;
        GlUtil.glBlendFunc(770, 771);
        GlUtil.glDisable(2896);
        GlUtil.glDisable(2903);
        GlUtil.glDisable(3553);
        GlUtil.glEnable(2903);
        GlUtil.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long var1) {
        this.seed = var1;
    }

    public SectorType getSystemTypeAt(Vector3i var1) {
        if (!USE_GALAXY) {
            return SectorType.SUN;
        } else {
            var1 = getLocalCoordinatesFromSystem(var1, this.tmp);
            return this.galaxyMap[var1.z][var1.y][var1.x] > this.threshHold ? SectorType.SUN : SectorType.VOID;
        }
    }

    public int getNumberOfStars() {
        return this.numberOfStars;
    }

    public IntArrayList getStarIndices() {
        return this.starIndices;
    }

    public FactionResourceRequestContainer getSystemResources(Vector3i var1, FactionResourceRequestContainer var2, GalaxyTmpVars var3) {
        var1 = getLocalCoordinatesFromSystem(var1, var3.tmp);
        return this.getSystemResourcesFromLocal(var1, var2, var3);
    }

    public FactionResourceRequestContainer getSystemResourcesFromLocal(Vector3i var1, FactionResourceRequestContainer var2, GalaxyTmpVars var3) {
        for(int var5 = 0; var5 < var2.res.length; ++var5) {
            float var4 = this.resourceNoises[var5].a((float)var1.x, (float)var1.y, (float)var1.z);
            var2.res[var5] = (byte)((int)((double)var4 * 127.0D));
        }

        return var2;
    }

    public float getSunIntensityFromSec(Vector3i var1) {
        var1 = getLocalCoordinatesFromSystem(StellarSystem.getPosFromSector(var1, new Vector3i()), new Vector3i());
        return this.getSystemSunIntensity(var1);
    }

    public float getSunIntensityFromSys(Vector3i var1) {
        var1 = getLocalCoordinatesFromSystem(var1, new Vector3i());
        return this.getSystemSunIntensity(var1);
    }

    public float getSunDistance(Vector3i var1) {
        Vector3i var2 = getLocalCoordinatesFromSystem(StellarSystem.getPosFromSector(var1, new Vector3i()), new Vector3i());
        int var3 = this.getSystemType(var2);
        var2 = this.getSunPositionOffset(var2, new Vector3i());
        Vector3i var4;
        (var4 = new Vector3i(8, 8, 8)).add(var2);
        var1 = StellarSystem.getLocalCoordinates(var1, new Vector3i());
        Vector3i var5;
        (var5 = new Vector3i()).sub(var4, var1);
        float var8 = var5.length();
        if (var3 == 3) {
            var2 = VoidSystem.getSecond(var4, var2, new Vector3i());
            Vector3i var7;
            (var7 = new Vector3i()).sub(var2, var1);
            float var6 = var7.length();
            var8 = Math.min(var8, var6);
        } else if (var3 == 1) {
            var8 = Math.max(0.0F, var8 - 1.44F);
        }

        return var8;
    }

    public boolean isVoid(Vector3i var1) {
        return this.isVoid(var1.x, var1.y, var1.z);
    }

    public boolean isVoidAbs(Vector3i var1) {
        return this.isVoid(var1.x + halfSize, var1.y + halfSize, var1.z + halfSize);
    }

    public boolean isVoid(int var1, int var2, int var3) {
        return this.galaxyMap[var3][var2][var1] <= this.threshHold;
    }

    public String getName(int var1, int var2, int var3) {
        if (this.isVoid(var1, var2, var3)) {
            return "Void";
        } else {
            long var4 = (long)(var3 * sizeSquared + var2 * size + var1);
            String var6;
            if ((var6 = (String)this.nameCache.get(var4)) == null) {
                long var7 = this.getSystemSeed(var1, var2, var3);
                this.random.setSeed(var7);
                this.nameGenerator.setSeed(var7);
                var6 = this.nameGenerator.compose(this.random.nextInt(6) + 1);
                this.nameCache.put(var4, var6);
            }

            return var6;
        }
    }

    public String getName(Vector3i var1) {
        return this.getName(var1.x, var1.y, var1.z);
    }

    public NPCFactionManager getNpcFactionManager() {
        assert this.npcFactionManager != null : "Can only be called on server";

        return this.npcFactionManager;
    }

    public void setNpcFactionManager(NPCFactionManager var1) {
        this.npcFactionManager = var1;
    }

    public PositionableSubSpriteCollectionReal getSpriteCollection() {
        return this.spriteCollection;
    }

    static {
        halfSize = size / 2;
        USE_GALAXY = false;
    }

    class CComp implements Comparator<StarPosition> {
        public Vector3f pos;

        private CComp() {
            this.pos = new Vector3f();
        }

        public int compare(StarPosition var1, StarPosition var2) {
            return Float.compare(var2.getDistanceToCam(), var1.getDistanceToCam());
        }
    }
}
