# Knowledge-is-Power (KIP)

A lightweight Fabric mod that lets players **learn super-powers from enchanted books** and cast them with a single key.  
No GUI, no clutter – just hold the book, right-click to learn, press **R** to unleash.

---

## 🌟 Features

| Power                  | Book | Effect                               | Cool-down |
|------------------------|------|--------------------------------------|-----------|
| **Flame Burst**        | Enchanted Book – Flame | Ignite what you’re looking at        | 10 s |
| **Haste**              | Enchanted Book – Efficiency | Haste, Speed and instant-mining      | 10 s |
| **Flight**             | Enchanted Book – Feather Falling | Flight                               | 15 s |
| **Lightning Strike**   | Enchanted Book – Channeling | Call a lightning bolt where you look | 10 s |
| **Mending**            | Enchanted Book – Mending | Heals hearts using experience        | Passive |
| **Curse of Vanishing** | Enchanted Book – Curse of Vanishing| Invisibility | 10s|
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
## 🧪 For Pack-Makers & Addon Devs

### ✅ Add a new enchantment → existing power (no code)
1. Create a **data-pack** (or include in your mod’s `data/`)
2. Add the enchantment to the desired tag:  
   `data/kipmod/tags/enchantment/grants_flame_ability.json`
   ```json
   {
     "replace": false,
     "values": [
       "your_mod:blazing_touch"
     ]
   }
   ```
   Done! Any book with `your_mod:blazing_touch` now grants **Flame Burst**.

---

### ✅ Add a brand-new ability (tiny Java snippet)

1. Implement `Abilities`:
   ```java
   public class VoidBlinkAbility implements Abilities {
       public String getId() { return "void_blink"; }
       public void activate(ServerPlayerEntity player){
           // your logic
       }
       // …other methods
   }
   ```
   Register it:
```java
AbilityRegistry.register("void_blink", new VoidBlinkAbility());
```
Map it to an enchantment (tag or fallback map) → players can now learn it like any other book.

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
