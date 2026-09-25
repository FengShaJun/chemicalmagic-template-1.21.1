package com.example.examplemod.Spells;
import com.example.examplemod.ChemicalMagic;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import net.minecraft.resources.ResourceLocation;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.capabilities.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;



public  class HeadMountedGasBottleSpell extends AbstractSpell{
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(20)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return ResourceLocation.fromNamespaceAndPath(ChemicalMagic.MOD_ID, "head_mounted_gas_bottle");
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        //从玩家视线前方查找目标实体
        LivingEntity target = findTarget(entity, 48.0);
        if (target != null) {
            // 将目标实体数据写入 MagicData，后续 onCast 中即可读取
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(target));
            return true;
        }
        return false;
    }

    @Override
    public  void onCast(Level level,int spellLevel,LivingEntity entity,CastSource castSource,MagicData playerMagicData){
        if (playerMagicData.getAdditionalCastData() instanceof  TargetEntityCastData targetData){
            LivingEntity target = targetData.getTarget(level);
            if (target != null){

            }
        }
    }
    private LivingEntity findTarget(LivingEntity caster, double range){
        Vec3 start = caster.getEyePosition();
        Vec3 end = start.add(caster.getLookAngle().scale(range));

    var entities = caster.level().getEntities(caster,
            caster.getBoundingBox().inflate(range),
            e -> e instanceof LivingEntity && e != caster && e.isAlive());

        LivingEntity closest = null;
        double closestDist = range * range;
        for (var e :entities){
            double dist = e.distanceToSqr(caster);
            if (dist <closestDist){
                // 可在此加入视线检测，避免穿墙锁定
                closestDist = dist;
                closest = (LivingEntity) e;
            }
        }
        return closest;
    }
}