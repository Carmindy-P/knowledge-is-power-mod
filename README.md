# Knowledge-is-Power (KIP)

A lightweight Fabric mod that lets players **learn super-powers from enchanted books** and cast them with a single key.  
No GUI, no clutter – just hold the book, right-click to learn, press **R** to unleash.

---

## 🌟 Features

| Power | Book | Effect | Cool-down |
|-------|------|--------|-----------|
| **Flame Burst** | Enchanted Book – Flame | Ignite the entity/block you’re looking at | 10 s |
| **Haste** | Enchanted Book – Efficiency | 10 s of **Haste V**, **Speed** and instant-mining | 10 s |
| **Flight** | Enchanted Book – Feather Falling | 15 s creative flight + 10 s slow-falling after expiry | 15 s |
| **Lightning Strike** | Enchanted Book – Channeling | Call a lightning bolt on the target | 10 s |
| **Mending** | Enchanted Book – Mending | Heals hearts using experience | Passive |

*More powers can be added by any mod or data-pack – the system is 100 % data-driven.*

---

## 🎮 How to Play

1. **Craft / loot / enchant** any vanilla “Enchanted Book” with one of the enchantments above.
2. **Hold the book in your main-hand** and **right-click** → ability is learned, book is consumed (unless in creative).
3. Press **R** (default) to cast.
4. A small cooldown indicator appears in the hot-bar; another message tells you when the power is ready again.

---

## 🛠 Server Admin Notes

- **Pure server-side** – clients without the mod will be kicked, so distribute the jar to everyone.
- **No config file yet** – balancing is done via code (cooldown, duration).
- **Permissions** – any player can use any power; OP can revoke with `/clear` or `/effect clear`.
- **Performance** – all ray-casts are limited to 10 blocks and run only on use.

---

## 🧪 Debug Commands (OP only)

| Command | Purpose |
|---------|---------|
| `/kipmod debugbook` | Dumps full NBT of the book you’re holding to server console – useful when creating custom books. |

---

## 🔧 For Developers

### Add your own power in 3 steps

1. Implement `Abilities`

```java
public class MyPower implements Abilities {
    public String getId() { return "my_power"; }
    public void activate(ServerPlayerEntity player){
        // your logic
    }
    // …other methods
}
```
## Register it

Add to `ModAbilities`:
```java
public static final MyPower MY_POWER = new MyPower();
```
In `ModAbilities.register()`:
```java
AbilityRegistry.register(MY_POWER.getId(), MY_POWER);
```

Map the enchant in AbilityBookComponent.ENCHANT_TO_ABILITY:
```java
"minecraft:my_enchant", "my_power"
```
That’s it – the existing book-learning, keybind and cooldown code works automatically.

---

## 📦 Installation
1. Install **Fabric-Loader ≥ 0.15** on client and server.  
2. Drop `knowledge-is-power-mod-x.x.x.jar` into the `mods` folder.  
3. *(Optional but recommended)* install **Fabric-API**.  
4. Start the game – no further configuration required.

---

## 🔗 Links

| What            | URL                                      |
|-----------------|------------------------------------------|
| Source & Issues | https://github.com/yourname/KnowledgeIsPower |
| CurseForge      | *(add once uploaded)*                    |
| Discord         | *(add once created)*                     |

---

## 📄 License
**MIT** – do whatever you want, just give credit.  
Made with Fabric by **Carmindy**
