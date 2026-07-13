# CLAUDE.md — ae3.sdk

AE3 SDK Package: core L2 target-context framework and shared helpers. Requires `ae3.api`; provides `ae3.sdk`.

## Existing JAXP Transformer, used only for serialization — deliberately not extended for XSLT application

`java/ru/myx/ae3/help/Dom.java` holds a shared `TransformerFactory`/`Transformer`: `Dom.transformerFactory` (built via `TransformerFactory.newInstance()`). Every call site (around lines 378-555) does `Dom.transformerFactory.newTransformer()` with no stylesheet argument — that's an identity transform, used purely to serialize this codebase's own DOM documents to bytes/text. `Dom`'s whole API is shaped around that one job (`Element` in, `String`/`Writer`/`TransferCopier` out).

Considered and rejected: adding `Templates`/`Source`/`Result`-typed methods here to apply an *external* stylesheet (e.g. for server-side XSLT rendering, see `ae3.sys.pkg.l2.tgt.xml`'s CLAUDE.md). `Dom` is depended on broadly; that capability had exactly one consumer. Ended up as its own private `TransformerFactory` inside the one class that actually needs it (`ru.myx.ae3.util.fn.SupplierVfsFolderXslTemplatesCached` in `ae3.sys.pkg.base`) instead — don't reintroduce it here without a second real consumer.

## Base.forUnknown wraps a plain Java object as a BaseObject on its own

`Base.forUnknown(object)` (`ru.myx.ae3.base.Base`, delegates to `Base.BASE_IMPL.javaObjectToBaseObject(object)`) turns an arbitrary Java object (e.g. a compiled `javax.xml.transform.Templates`) into a `BaseObject` by itself, unwrapped later via `.baseValue()` — no extra `BaseNativeObject` wrapping needed for an opaque cache payload. (The `BaseNativeObject(Base.forUnknown(AcmTplLanguageImpl.INSTANCE))` pattern seen in `ae3.sdk-lang.acm-tpl` is a different thing — it makes the wrapped instance the *prototype* of a new empty object, for reflective JS method access, not what a plain cache value needs.)
