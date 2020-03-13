package $package$.application

import $package$.domain._
import zio.ZIO

object ApplicationService {

  def addItem(name: String, price: BigDecimal): ZIO[ItemRepository, DomainError, ItemId] =
    ZIO.accessM[ItemRepository](_.itemRepository.add(name, price))

  def deleteItem(itemId: ItemId): ZIO[ItemRepository, DomainError, Unit] =
    ZIO.accessM[ItemRepository](_.itemRepository.delete(itemId))

  def getItem(itemId: ItemId): ZIO[ItemRepository, DomainError, Option[Item]] =
    ZIO.accessM[ItemRepository](_.itemRepository.getById(itemId))
    
  val getItems: ZIO[ItemRepository, DomainError, List[Item]] =
    ZIO.accessM[ItemRepository](_.itemRepository.getAll)

  def partialUpdateItem(itemId: ItemId, name: Option[String], price: Option[BigDecimal]): ZIO[ItemRepository, DomainError, Option[Unit]] =
    (for {
      item <- ZIO.accessM[ItemRepository](_.itemRepository.getById(itemId)).some
      _    <- ZIO.accessM[ItemRepository](_.itemRepository.update(itemId, name.getOrElse(item.name), price.getOrElse(item.price))).mapError(Some(_))
    } yield ()).optional

  def updateItem(itemId: ItemId, name: String, price: BigDecimal): ZIO[ItemRepository, DomainError, Option[Unit]] =
    ZIO.accessM[ItemRepository](_.itemRepository.update(itemId, name, price))

}
