package dev.lukv.farmingParty.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.lukv.farmingParty.client.ProfitHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "at.hannibal2.skyhanni.features.garden.tracker.PestProfitTracker", remap = false)
public class PestProfitTrackerMixin {
    @Inject(
            method = "drawDisplay",
            at = @At(
                    value = "INVOKE",
                    target = "Lat/hannibal2/skyhanni/features/garden/tracker/PestProfitTracker$BucketData;getSelectedBucket()Ljava/lang/Enum;",
                    ordinal = 0
            )
    )
    private void farmingParty$afterProfit(CallbackInfoReturnable<List<?>> cir, @Local(name = "profit") double profit) {
        ProfitHolder.INSTANCE.setPestProfit(profit);
    }
}