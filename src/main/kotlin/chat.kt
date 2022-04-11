import java.util.*

fun main() {

    ChatService.createChat(
        "Чат 1",
        1,
        arrayListOf(1, 3, 4),
        PushSettings(false, 0)
    )

    ChatService.createChat(
        "Чат 2",
        3,
        arrayListOf(10, 9, 4),
        PushSettings(false, 0)

    )

    ChatService.createChat(
        "Чат 3",
        2,
        arrayListOf(1, 2, 4),
        PushSettings(false, 0)
    )

    ChatService.createChat(
        "Чат 4",
        3,
        arrayListOf(3, 7, 4),
        PushSettings(false, 0)
    )

//    ChatService.deleteChat(2)
//    ChatService.deleteChat(1)
//    ChatService.restoreChat(2)
    val chats = ChatService.getChats()

    chats.forEach { chat ->
        println(chat)
    }

    ChatService.createMessage(
        1,
        2,
        2,
        readState = false,
        out = false,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )
    ChatService.createMessage(
        1,
        2,
        2,
        readState = false,
        out = false,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )
    ChatService.createMessage(
        1,
        2,
        2,
        readState = true,
        out = false,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )
    ChatService.createMessage(
        1,
        2,
        2,
        readState = false,
        out = true,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )

    ChatService.createMessage(
        1,
        2,
        1,
        readState = true,
        out = false,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )
    ChatService.createMessage(
        1,
        2,
        1,
        readState = false,
        out = true,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )


    ChatService.createMessage(
        1,
        2,
        3,
        readState = true,
        out = false,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )
    ChatService.createMessage(
        1,
        2,
        3,
        readState = true,
        out = true,
        "Сообщение 1",
        "Текст сообщения 1",
        important = false,
        deleted = false
    )

    val chats2 = ChatService.getUnreadChats(2)
    val chats3 = ChatService.getChatsWithMessage()

    println("----------")
    println("Непрочитанных чатов: ${ChatService.getUnreadChatsCount(2)}")

    chats2.forEach { chat ->
        println(chat)
    }

    println("----------")
    println("Чатов с сообщениями: ${chats3.size}")

    chats3.forEach { chat ->
        println(chat)
    }

    chats3.forEach { chat ->
        println("Чат ${chat.id!!}")
        println(ChatService.getChatMessages(chatId = chat.id, count = 2))
        println("   ")
    }

    println("----------")
    println("Непрочитанных чатов: ${ChatService.getUnreadChatsCount(2)}")
}

data class Message(
    var id: Int,
    val userId: Int,
    val fromId: Int,
    var chatId: Int,
    val date: Long,
    var readState: Boolean,
    val out: Boolean,
    var title: String,
    var body: String,
    var important: Boolean,
    var deleted: Boolean,
)

data class Chat(
    val id: Int?,
    var title: String,
    val adminId: Int,
    var users: ArrayList<Int>,
    var pushSettings: PushSettings,
    var photo50: String?,
    var photo100: String?,
    var photo200: String?,
    var left: Boolean,
    var kicked: Boolean,
)

data class PushSettings(
    val sound: Boolean,
    val disabledUntil: Int,
)

object ChatService {
    private var chatId: Int = 0
    private var messageId: Int = 0
    private var chats: MutableList<Chat> = mutableListOf<Chat>()
    private var deletedChats: MutableList<Chat> = mutableListOf<Chat>()
    private var messages: MutableList<Message> = mutableListOf<Message>()

    fun init() {
        chatId = 0
        messageId = 0
        chats = mutableListOf<Chat>()
        deletedChats = mutableListOf<Chat>()
        messages = mutableListOf<Message>()
    }

    fun createChat(
        title: String,
        adminId: Int,
        users: ArrayList<Int>,
        pushSettings: PushSettings,
        photo50: String? = null,
        photo100: String? = null,
        photo200: String? = null,
        left: Boolean = false,
        kicked: Boolean = false,
    ): Int {
        val id = chatId + 1
        val chatItem = Chat(id, title, adminId, users, pushSettings, photo50, photo100, photo200, left, kicked)
        chats.add(chatItem)
        chatId = id
        return id
    }

    fun getDeletedChats(): List<Chat> {
        return deletedChats
    }

    fun deleteChat(id: Int) {
        val chatItem = chats.find { it.id == id }
        if (chatItem != null) {
            deletedChats.add(chatItem)
            chats.remove(chatItem)
            val messagesForDelete = messages.filter { message -> message.chatId == id }
            messagesForDelete.forEach { message ->
                message.deleted = true
            }
        }
    }

    fun restoreChat(id: Int) {
        val chatItem = deletedChats.find { it.id == id }
        if (chatItem != null) {
            chats.add(chatItem)
            deletedChats.remove(chatItem)
        }
    }

    fun getChats(ids: ArrayList<Int>? = null): List<Chat> {
        return if (ids.isNullOrEmpty()) chats else chats.filter { ids.contains(it.id) }
    }

    fun getChatsWithMessage(): List<Chat> {
        val ids = arrayListOf<Int>()
        messages.forEach { message -> if (!ids.contains(message.chatId)) ids.add(message.chatId) }
        return chats.filter { ids.contains(it.id) }
    }


    fun createMessage(
        userId: Int,
        fromId: Int,
        chatId: Int,
        readState: Boolean,
        out: Boolean,
        title: String,
        body: String,
        important: Boolean,
        deleted: Boolean,
    ): Int {
        val id = messageId + 1
        val date = Date().time
        val messageItem = Message(id, userId, fromId, chatId, date, readState, out, title, body, important, deleted)
        messages.add(messageItem)
        messageId = id
        if (getChats(arrayListOf(chatId)).isEmpty()) {
            createChat("", fromId, arrayListOf(userId, fromId), PushSettings(false, 0), left = false, kicked = false)
        }
        return id
    }

    fun deleteMessage(
        id: Int,
    ) {
        val message = messages.find { it.id == id }
        if (message?.deleted == false) {
            message.deleted = true

            if (getChatMessages(message.chatId).isEmpty()) {
                deleteChat(message.chatId)
            }

        }
    }

    fun restoreMessage(
        id: Int,
    ) {
        val message = messages.find { it.id == id }
        if (message?.deleted == true) message.deleted = false
    }

    fun editMessage(
        id: Int,
        chatId: Int? = null,
        readState: Boolean? = null,
        title: String? = null,
        body: String? = null,
        important: Boolean? = null,
    ): Message? {
        val message = messages.find { it.id == id }
        if (message != null) {
            if (chatId != null) message.chatId = chatId
            if (readState != null) message.readState = readState
            if (title != null) message.title = title
            if (body != null) message.body = body
            if (important != null) message.important = important
        }
        return message
    }

    private fun getUnreadChatsIds(userId: Int): List<Int> {
        val chatsIds = arrayListOf<Int>()
        messages.forEach { message ->
            if (!message.readState && ((message.out && message.fromId != userId) || (!message.out && message.userId != userId))) {
                if (!chatsIds.contains(message.chatId)) chatsIds.add(message.chatId)
            }
        }
        return chatsIds
    }

    fun getUnreadChats(userId: Int): List<Chat> {
        val chatsIds = getUnreadChatsIds(userId)
        return chats.filter { chatsIds.contains(it.id) }
    }

    fun getUnreadChatsCount(userId: Int): Int {
        val chatsIds = getUnreadChatsIds(userId)
        return chatsIds.size
    }

    fun getChatMessages(chatId: Int, lastId: Int = 0, count: Int? = null): List<Message> {
        val messagesTemp = arrayListOf<Message>()
        var index = 0
        messages.forEach { message ->
            if (message.chatId == chatId && !message.deleted && message.id >= lastId && (count == null || index < count)) {
                messagesTemp.add(message)
                index++
                message.readState = true
            }
        }
        return messagesTemp
    }

    fun getAllMessages(): List<Message> {
        return messages.filter { !it.deleted }
    }

}