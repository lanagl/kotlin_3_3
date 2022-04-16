import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

val chat1 = Chat(id = null,
    "Чат 1",
    1,
    arrayListOf(1, 3, 4),
    PushSettings(false, 0),
    null, null, null,
    left = false,
    kicked = false
)

val chat2 = Chat(id = null,
    "Чат 2",
    3,
    arrayListOf(10, 9, 4),
    PushSettings(false, 0),
    null, null, null,
    left = false,
    kicked = false
)


val title = "Сообщение "
val text = "Текст сообщения "

private var resultChat = 0

class ChatServiceTest {

    @Before
    fun prepareGetChatsWithMessage() {
        ChatService.init()
        ChatService.createChat(chat2.title,
            chat2.adminId,
            chat2.users,
            chat2.pushSettings,
            chat2.photo50,
            chat2.photo100,
            chat2.photo200,
            chat2.left,
            chat2.kicked)
        ChatService.createChat(chat1.title,
            chat1.adminId,
            chat1.users,
            chat1.pushSettings,
            chat1.photo50,
            chat1.photo100,
            chat1.photo200,
            chat1.left,
            chat1.kicked)
        ChatService.createMessage(
            1,
            2,
            2,
            readState = false,
            out = false,
            title = "$title 1",
            body = "$text 1",
            important = false,
            deleted = false)
        ChatService.createMessage(
            1,
            2,
            2,
            readState = false,
            out = false,
            title = "$title 2",
            body = "$text 2",
            important = false,
            deleted = false)

        ChatService.createMessage(
            1,
            2,
            2,
            readState = false,
            out = false,
            title = "$title 3",
            body = "$text 3",
            important = false,
            deleted = false)
        ChatService.createMessage(
            1,
            2,
            1,
            readState = false,
            out = true,
            title = "$title 4",
            body = "$text 4",
            important = false,
            deleted = false)
        ChatService.createMessage(
            1,
            2,
            1,
            readState = false,
            out = false,
            title = "$title 5",
            body = "$text 5",
            important = false,
            deleted = false)
        ChatService.createMessage(
            1,
            2,
            3,
            readState = false,
            out = true,
            title = "$title 6",
            body = "$text 6",
            important = false,
            deleted = false)
        ChatService.createMessage(
            1,
            2,
            3,
            readState = true,
            out = false,
            title = "$title 7",
            body = "$text 7",
            important = false,
            deleted = false
        )
        ChatService.createMessage(
            1,
            2,
            3,
            readState = true,
            out = true,
            title = "$title 8",
            body = "$text 8",
            important = false,
            deleted = false
        )
    }

    @Test
    fun getMessages() {
        val result = ChatService.getChatMessages(1).size
        Assert.assertEquals(2, result)
    }

    @Test
    fun createChat() {
        val result = ChatService.createChat(chat1.title,
            chat1.adminId,
            chat1.users,
            chat1.pushSettings,
            chat1.photo50,
            chat1.photo100,
            chat1.photo200,
            chat1.left,
            chat1.kicked)
        Assert.assertNotEquals(0, result)
    }


    @Test
    fun restoreChat_deleted() {
        ChatService.deleteChat(resultChat)
        ChatService.restoreChat(resultChat)
        val result = ChatService.getDeletedChats().size
        Assert.assertEquals(0, result)
    }

    @Test
    fun restoreChat_chats() {
        ChatService.deleteChat(resultChat)
        ChatService.restoreChat(resultChat)
        val result = ChatService.getChats().size
        Assert.assertEquals(3, result)
    }

    @Test
    fun getChats() {
        val result = ChatService.getChats().size
        Assert.assertEquals(3, result)
    }


    @Test
    fun getChatsWithMessage() {
        val result = ChatService.getChatsWithMessage().size
        Assert.assertEquals(3, result)
    }

    @Test
    fun createMessage() {
        ChatService.createMessage(
            1,
            2,
            1,
            readState = false,
            out = false,
            title = "$title 9",
            body = "$text 9",
            important = false,
            deleted = false)
        val result = ChatService.getChatMessages(1).size
        Assert.assertEquals(3, result)
    }

    @Test
    fun deleteMessage() {
        val resultBefore = ChatService.getAllMessages().size
        ChatService.deleteMessage(3)
        val resultAfter = ChatService.getAllMessages().size
        Assert.assertEquals(resultBefore, resultAfter + 1)
    }

    @Test
    fun restoreMessage() {
        val id = ChatService.createMessage(
            1,
            2,
            1,
            readState = false,
            out = false,
            title = "$title 10",
            body = "$text 10",
            important = false,
            deleted = false)
        ChatService.deleteMessage(id)
        val resultBefore = ChatService.getAllMessages().size
        ChatService.restoreMessage(id)
        val resultAfter = ChatService.getAllMessages().size
        Assert.assertEquals(resultBefore + 1, resultAfter)
    }

    @Test
    fun editMessage() {
        val date = Date().time
        val message =
            Message(1111,
                1,
                2,
                3,
                date,
                readState = false,
                out = false,
                title = "$title 11",
                body = "$text 11",
                important = false,
                deleted = false)
        val id = ChatService.createMessage(message.userId,
            message.fromId,
            message.chatId,
            message.readState,
            message.out,
            message.title,
            message.body,
            message.important,
            message.deleted)

        message.id = id

        val newMessage = ChatService.editMessage(id, title = "new title")

        Assert.assertNotEquals(message, newMessage)
    }

    @Test
    fun getUnreadChats() {
        val result = ChatService.getUnreadChats(1).size
        Assert.assertEquals(2, result)
    }

    @Test
    fun getUnreadChatsCount() {
        val result = ChatService.getUnreadChatsCount(1)
        Assert.assertEquals(2, result)
    }

    @Test
    fun getChatMessages() {
        val result = ChatService.getChatMessages(1).size
        Assert.assertEquals(2, result)
    }

    @After
    fun clearChats() {
        ChatService.init()
    }

}