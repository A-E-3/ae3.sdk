# CLAUDE.md — ae3.sdk

AE3 SDK Package: core L2 target-context framework and shared helpers. Requires `ae3.api`; provides `ae3.sdk`.

## Existing JAXP Transformer, used only for serialization

`java/ru/myx/ae3/help/Dom.java` holds a shared `TransformerFactory`/`Transformer`: `Dom.transformerFactory` (built via `TransformerFactory.newInstance()`). Every current call site (around lines 378-555) does `Dom.transformerFactory.newTransformer()` with no stylesheet argument — that's an identity transform, used purely to serialize a DOM to bytes/text. Nothing in this codebase currently loads an external `.xsl` stylesheet into a `Transformer` and applies it to a document.

This is the natural place to add a real transform helper — matching `Dom`'s own `toXxx(...)` static-factory convention (`toDocument`, `toElement`, `toXmlCompact`...), so e.g. `Dom.toTemplates(Source xsl)` (compile) and `Dom.transform(Source xml, Templates templates, Result out)` (apply), both `public static final`, both referencing `Dom.transformerFactory` class-qualified like every other method here, errors wrapped the same way as `toXmlCompact*`: `throw Report.MODE_ASSERT || Report.MODE_DEBUG ? new Error(e) : new Error(e.getMessage())`. See `ae3.sys.pkg.i3.web`'s CLAUDE.md for the request-handling side that would consume it.

## Caching a compiled Templates: reuse SupplierVfsFileAbstractTextParseCached, don't hand-roll BaseSupplierCachedLazy

AE3 already has a purpose-built hierarchy in `ae3.sys.pkg.base`'s `ru.myx.ae3.util.fn` package for exactly "VFS text file → parsed/compiled cached `BaseObject`", and it's simpler than the generic `BaseSupplierCachedLazy` underneath it:

- `SupplierTextInputAbstractTextCached` (`get()`, final): re-checks at most every 2500ms (`Engine.fastTime()`); re-parses only when the freshly-loaded source text actually differs from `this.lastSource` (plain content-string equality, no separate mtime/checksum bookkeeping needed). Subclasses implement `loadSource()` and `parseText(CharSequence)`.
- `SupplierVfsFileAbstractTextParseCached` — implements `loadSource()` as `this.file.getTextContent().baseValue()` for a given `Entry`. Still abstract on `parseText`.
- `SupplierVfsFileXmlToMapCached` — the concrete sibling to model from: constructor takes the `Entry`, `parseText(CharSequence)` does the real work and returns a `BaseObject`.

So the compiled-`Templates` cache should be a new sibling class, e.g. `SupplierVfsFileXslTemplatesCached extends SupplierVfsFileAbstractTextParseCached`, whose `parseText(CharSequence)` strips the ACM.TPL wrapper (see `ae3.sys.pkg.l2.tgt.xml`'s CLAUDE.md) and calls the new `Dom.toTemplates(...)`.

Wrapping the result: `Base.forUnknown(object)` (`ru.myx.ae3.base.Base`, delegates to `Base.BASE_IMPL.javaObjectToBaseObject(object)`) already turns an arbitrary Java object into a `BaseObject` on its own, unwrapped later via `.baseValue()` — no extra `BaseNativeObject` wrapping needed for an opaque cache payload. (The `BaseNativeObject(Base.forUnknown(AcmTplLanguageImpl.INSTANCE))` pattern seen in `ae3.sdk-lang.acm-tpl` is a different thing — it makes the wrapped instance the *prototype* of a new empty object, for reflective JS method access, not what a plain cache value needs.)
